package com.team1.spreet.domain.feed.service;

import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.domain.feed.dto.FeedDto;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedImage;
import com.team1.spreet.domain.feed.repository.FeedCommentRepository;
import com.team1.spreet.domain.feed.repository.FeedImageRepository;
import com.team1.spreet.domain.feed.repository.FeedLikeRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final AwsS3Service awsS3Service;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedImageRepository imageRepository;
    private final AlarmService alertService;
    private final SubscribeRepository subscribeRepository;
    private final FeedCommentRepository feedCommentRepository;

    //feed 최신순 조회
    @Transactional(readOnly = true)
    public List<FeedDto.ResponseDto> getRecentFeed(int page, int size, Long userId) {
        //pageable 속성값 설정
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        List<Feed> feedList = feedRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable).getContent();    //페이징한 feed 리스트
        //반환할 feedList 생성
        List<FeedDto.ResponseDto> recentFeedList = new ArrayList<>();
        for (Feed feed : feedList) {
            Long feedLike = feedLikeRepository.countByFeedId(feed.getId());    //좋아요 개수 조회
            //저장된 이미지 조회
            List<String> imageUrlList = getFeedImageUrlList(feed.getId());
            //로그인 여부에 따라 좋아요 추가
            if (userId != 0L) {
                boolean liked = feedLikeRepository.existsByUserIdAndFeed(userId, feed);    //좋아요 여부 확인
                recentFeedList.add(new FeedDto.ResponseDto(feed, imageUrlList, feedLike, liked));
            }else{
                recentFeedList.add(new FeedDto.ResponseDto(feed, imageUrlList, feedLike, false));
            }
        }
        return recentFeedList;
    }
    @Transactional(readOnly = true)
    public List<FeedDto.SimpleResponseDto> getSimpleFeed() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);
        List<Feed> feedList = feedRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable).getContent();

        List<FeedDto.SimpleResponseDto> simpleFeedList = new ArrayList<>();

        for (Feed feed : feedList) {
            simpleFeedList.add(new FeedDto.SimpleResponseDto(feed));
        }
        return simpleFeedList;
    }
    //feed 조회
    @Transactional(readOnly = true)
    public FeedDto.ResponseDto getFeed(Long feedId, Long userId) {
        Feed feed = checkFeedWithUser(feedId);
        Long feedLikeCount = feedLikeRepository.countByFeedId(feedId);    //좋아요 개수 조회
        List<String> imageUrlList = getFeedImageUrlList(feedId);
        //로그인 여부 및 좋아요 여부 확인
        if (userId != 0L) {
            boolean liked = feedLikeRepository.existsByUserIdAndFeed(userId, feed);
            return new FeedDto.ResponseDto(feed, imageUrlList, feedLikeCount, liked);
        }else{
            return new FeedDto.ResponseDto(feed, imageUrlList, feedLikeCount, false);
        }
    }
    //feed 저장
    @Transactional
    public void saveFeed(FeedDto.RequestDto requestDto, User user) {
        Feed feed = requestDto.toEntity(user);    //feed 엔티티 초기화
        feedRepository.save(feed);    //feed 저장
        saveImage(requestDto.getFile(), feed);    //새로운 이미지 저장
        alarmToSubscriber(user, feed);
    }
    //feed 수정
    @Transactional
    public void updateFeed(Long feedId, FeedDto.RequestDto requestDto, User user) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기
        if(checkOwner(feed, user)){
            feed.update(requestDto.getTitle(), requestDto.getContent());
            //이미지가 있다면 새로운 이미지 저장
            if(!requestDto.getFile().isEmpty()) {
                deleteImage(feedId);    //기존에 업로드된 이미지 제거
                saveImage(requestDto.getFile(), feed);
            }
        }
    }
    //feed 삭제
    @Transactional
    public void deleteFeed(Long feedId, User user) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기
        if(user.getUserRole() == UserRole.ROLE_ADMIN || checkOwner(feed, user)){
            deleteByFeedId(feed);
        }
    }

    //새로운 이미지 저장
    private void saveImage(List<MultipartFile> multipartFileList, Feed feed){
        if(!multipartFileList.isEmpty()) {
                for (MultipartFile multipartFile : multipartFileList) {
                String imageUrl = awsS3Service.uploadFile(multipartFile);
                FeedImage image = new FeedImage(imageUrl, feed);
                imageRepository.save(image);
            }
        }
    }
    //이미지 파일 삭제
    private void deleteImage(Long feedId){
        List<FeedImage> imageList = imageRepository.findByFeedId(feedId);
        if(!imageList.isEmpty()){
            for (FeedImage image : imageList) {
            String fileName = image.getImageUrl().replace("https://spreet-bucket.s3.ap-northeast-2.amazonaws.com/", "");
            awsS3Service.deleteFile(fileName);
            imageRepository.delete(image);
            }
        }
    }
    //feed 찾기
    private Feed checkFeed(Long feedId){
        return feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
    private Feed checkFeedWithUser(Long feedId){
        return feedRepository.findByIdWithUser(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }

    private boolean checkOwner(Feed feed, User user) {
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        } else {
            return true;
        }
    }
    private void deleteByFeedId(Feed feed) {
        feedCommentRepository.updateDeletedTrueByFeedId(feed.getId());
        feedLikeRepository.deleteByFeedId(feed.getId());    //좋아요 삭제
        deleteImage(feed.getId());    //기존에 업로드된 이미지 제거
        feed.isDeleted();    //feed 삭제
    }
    private List<String> getFeedImageUrlList(Long feedId) {
        List<String> imageUrlList = new ArrayList<>();
        List<FeedImage> imageList = imageRepository.findByFeedId(feedId);
        for (FeedImage image : imageList) {
            imageUrlList.add(image.getImageUrl());
        }
        return imageUrlList;
    }
    private void alarmToSubscriber(User user, Feed feed) {
        List<Subscribe> subscribes = subscribeRepository.findByPublisher(user).orElse(null);
        if(subscribes!=null){
            for (Subscribe subscribe : subscribes) {
                alertService.send(user.getId(),
                        "새로운 feed가 생성되었습니다"+System.lineSeparator()+user.getNickname()+": "+feed.getTitle(),
                        "localhost:8080/api/feed/"+feed.getId(),
                        subscribe.getSubscriber().getId());
            }
        }
    }
}


