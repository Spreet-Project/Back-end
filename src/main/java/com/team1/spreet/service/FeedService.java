package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.dto.FeedLikeDto;
import com.team1.spreet.entity.*;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final FeedCommentRepository feedCommentRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;
    private final SubscribeRepository subscribeRepository;

    //feed 최신순 조회
    @Transactional(readOnly = true)
    public List<FeedDto.ResponseDto> getRecentFeed(int page, int size, Long userId) {
        //pageable 속성값 설정
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        List<Feed> feedList = feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable).getContent();    //페이징한 feed 리스트
        //반환할 feedList 생성
        List<FeedDto.ResponseDto> recentFeedList = new ArrayList<>();
        for (Feed feed : feedList) {
            Long feedLike = feedLikeRepository.countByFeedId(feed.getId());    //좋아요 개수 조회
            //저장된 이미지 조회
            List<String> imageUrlList = new ArrayList<>();
            List<Image> imageList = imageRepository.findAllByFeedId(feed.getId());
            for (Image image : imageList) {
                imageUrlList.add(image.getImageUrl());
            }
            boolean isLike = feedLikeRepository.existsByUserIdAndFeed(userId, feed);    //좋아요 여부 확인
            //로그인 여부에 따라 좋아요 추가
            if (userId != 0L) {
                recentFeedList.add(new FeedDto.ResponseDto(feed, imageUrlList, feedLike, isLike));
            }else{
                recentFeedList.add(new FeedDto.ResponseDto(feed, imageUrlList, feedLike, false));
            }
        }
        return recentFeedList;
    }
    //feed 조회
    @Transactional(readOnly = true)
    public FeedDto.ResponseDto getFeed(Long feedId, Long userId) {
        Feed feed = isFeed(feedId);    //feedId로 feed 찾기
        Long feedLike = feedLikeRepository.countByFeedId(feedId);    //좋아요 개수 조회
        //저장된 이미지 조회
        List<String> imageUrlList = new ArrayList<>();
        List<Image> imageList = imageRepository.findAllByFeedId(feedId);
        for (Image image : imageList) {
            imageUrlList.add(image.getImageUrl());
        }
        //로그인 여부 및 좋아요 여부 확인
        if (userId != 0L) {
            boolean isLike = feedLikeRepository.existsByUserIdAndFeed(userId, feed);
            return new FeedDto.ResponseDto(feed, imageUrlList,feedLike, isLike);
        }else{
            return new FeedDto.ResponseDto(feed, imageUrlList,feedLike, false);
        }
    }
    //feed 저장
    @Transactional
    public SuccessStatusCode saveFeed(FeedDto.RequestDto requestDto, UserDetails userDetails) {
        User user = checkUser(Long.parseLong(userDetails.getUsername()));    //userId로 user 찾기
        Feed feed = requestDto.toEntity(user);    //feed 엔티티 초기화
        feedRepository.save(feed);    //feed 저장
        saveImage(requestDto.getFile(), feed);    //새로운 이미지 저장
        //구독자에게 알림 생성
        List<Subscribe> subscribes = subscribeRepository.findAllByPublisher(user).orElse(null);
        if(subscribes!=null){
            for (Subscribe subscribe : subscribes) {
                alertService.send(user.getId(),
                        "새로운 feed가 생성되었습니다"+System.lineSeparator()+user.getNickname()+": "+feed.getTitle(),
                        "localhost:8080/api/feed/"+feed.getId(),
                        subscribe.getSubscriber().getId());
            }
        }
        return SuccessStatusCode.SAVE_FEED;
    }
    //feed 수정
    @Transactional
    public SuccessStatusCode updateFeed(Long feedId, FeedDto.RequestDto requestDto, UserDetails userDetails) {
        User user = checkUser(Long.parseLong(userDetails.getUsername()));    //userId로 user 찾기
        Feed feed = isFeed(feedId);    //feedId로 feed 찾기
        if(checkOwner(feed, user)){
//            feed.update(requestDto.getTitle(), requestDto.getContent(), user);    //feed 내용 수정
            feedRepository.updateTitleAndContentByIdAndUser(requestDto.getTitle(), requestDto.getContent(), feedId, user);
            deleteImage(feedId);    //기존에 업로드된 이미지 제거
            //이미지가 있다면 새로운 이미지 저장
            if(requestDto.getFile() != null) {
                saveImage(requestDto.getFile(), feed);
            }
            return SuccessStatusCode.UPDATE_FEED;
        }else{
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
    }
    //feed 삭제
    @Transactional
    public SuccessStatusCode deleteFeed(Long feedId, UserDetails userDetails) {
        User user = checkUser(Long.parseLong(userDetails.getUsername()));    //userId로 user 찾기
        Feed feed = checkFeed(feedId, user.getId());    //userId, feedId로 feed 찾기
        if(checkOwner(feed, user)){
            feedCommentRepository.updateIsDeletedTrueByFeedId(feed);
            feedLikeRepository.deleteByFeed(feed);    //좋아요 삭제
            deleteImage(feedId);    //기존에 업로드된 이미지 제거
            feedRepository.updateIsDeletedTrueById(feedId);    //feed 삭제
        }
        return SuccessStatusCode.DELETE_FEED;
    }
    //feed 좋아요
    @Transactional
    public CustomResponseBody<FeedLikeDto.ResponseDto> likeFeed(Long feedId, UserDetails userDetails) {
        User user = checkUser(Long.parseLong(userDetails.getUsername()));    //userId로 user 찾기
        Feed feed = isFeed(feedId);    //feedId로 feed 찾기
        FeedLike feedLike = feedLikeRepository.findByUserAndFeed(user, feed).orElse(null);
        if (feedLike!=null) {
            feedLikeRepository.delete(feedLike);
            return new CustomResponseBody<>(SuccessStatusCode.CANCEL_LIKE_FEED, new FeedLikeDto.ResponseDto(false));
        }else{
            feedLikeRepository.save(new FeedLike(user, feed));
            return new CustomResponseBody<>(SuccessStatusCode.LIKE_FEED, new FeedLikeDto.ResponseDto(true));
        }
    }
    //새로운 이미지 저장
    private void saveImage(List<MultipartFile> multipartFileList, Feed feed){
        for (MultipartFile multipartFile : multipartFileList) {
            String imageUrl = awsS3Service.uploadFile(multipartFile);
            Image image = new Image(imageUrl, feed);
            imageRepository.save(image);
        }
    }
    //이미지 파일 삭제
    private void deleteImage(Long feedId){
        List<Image> imageList = imageRepository.findAllByFeedId(feedId);
        for (Image image : imageList) {
            String fileName = image.getImageUrl().replace("https://spreet-bucket.s3.ap-northeast-2.amazonaws.com/", "");
            awsS3Service.deleteFile(fileName);
            imageRepository.delete(image);
        }
    }
    //해당 유저가 작성한 feed 찾기
    private Feed checkFeed(Long feedId, Long userId){
        return feedRepository.findByIdAndUserIdAndIsDeletedFalse(feedId, userId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
    //feed 찾기
    private Feed isFeed(Long feedId){
        return feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
    //user 정보 가져오기
    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
    }
    private boolean checkOwner(Feed feed, User user) {
        if (user.getUserRole() == UserRole.ROLE_ADMIN || feed.getUser().equals(user)) {
            return true;
        } else {
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
    }
}


