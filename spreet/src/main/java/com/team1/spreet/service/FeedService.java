package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.entity.*;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.*;
import com.team1.spreet.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Transactional(readOnly = true)
    public CustomResponseBody getRecentFeed() {
        return new CustomResponseBody(SuccessStatusCode.GET_RECENT_FEED);
    }
    @Transactional(readOnly = true)
    public CustomResponseBody<FeedDto.ResponseDto> getFeed(Long feedId, UserDetailsImpl userDetailsImpl) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기
        User user = checkUser(userDetailsImpl);    //userDetailsImpl user 찾기
        Long feedLike = feedLikeRepository.countByFeedIdAndIsLikeTrue(feedId);
        FeedLike isLike = feedLikeRepository.findByUserIdAndFeedId(user.getId(), feedId);
        List<FeedComment> feedCommentList= feedCommentRepository.findAllByFeedId(feedId);
        FeedCommentDto.CommentListDto commentListDto = new FeedCommentDto.CommentListDto();
        for (FeedComment feedComment : feedCommentList) {
            commentListDto.addComment(new FeedCommentDto.ResponseDto(feedComment, user.getNickname()));
        }
        FeedDto.ResponseDto responseDto = new FeedDto.ResponseDto(feed, user.getNickname(), feedLike, isLike.isLike(), commentListDto);
        return new CustomResponseBody<FeedDto.ResponseDto>(SuccessStatusCode.GET_FEED, responseDto);
    }
    @Transactional
    public CustomResponseBody<SuccessStatusCode> saveFeed(FeedDto.RequestDto requestDto, UserDetails userDetails) {
        User user = userRepository.findById(Long.valueOf(userDetails.getUsername())).orElse(null);
        Feed feed = new Feed(requestDto.getTitle(), requestDto.getContent(), user);
        feedRepository.save(feed);    //feed 저장
        saveImage(requestDto.getFile(), feed);    //새로운 이미지 저장
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_FEED);
    }
    @Transactional
    public CustomResponseBody<SuccessStatusCode> updateFeed(Long feedId, FeedDto.RequestDto requestDto, User user) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기
        feed.update(requestDto.getTitle(), requestDto.getContent(), user);    //feed 내용 수정
        deleteImage(feedId);    //기존에 업로드된 이미지 제거
        saveImage(requestDto.getFile(), feed);    //새로운 이미지 저장
        return new CustomResponseBody<SuccessStatusCode>(SuccessStatusCode.UPDATE_FEED);
    }
    @Transactional
    public CustomResponseBody<SuccessStatusCode> deleteFeed(Long feedId, User user) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기        feed.setDeleted();  //feed delete
        //comment delete
        List<FeedComment> feedCommentList = feedCommentRepository.findAllByFeedId(feedId);
        for (FeedComment feedComment : feedCommentList) {
            feedComment.setDeleted();
        }
        deleteImage(feedId);    //기존에 업로드된 이미지 제거
        return new CustomResponseBody<SuccessStatusCode>(SuccessStatusCode.DELETE_FEED);
    }
    //feed 좋아요
    @Transactional
    public CustomResponseBody likeFeed(Long feedId, User user) {
        Feed feed = checkFeed(feedId);    //feedId로 feed 찾기
        FeedLike feedLike = feedLikeRepository.findByUserIdAndFeedId(user.getId(), feedId);
        if (feedLike == null) {
            FeedLike addFeedLike = new FeedLike(true, feed, user);
            feedLikeRepository.save(addFeedLike);
            return new CustomResponseBody(SuccessStatusCode.LIKE_FEED,addFeedLike.isLike());
        }else if(feedLike.isLike()){
            feedLike.clickLike();
            return new CustomResponseBody(SuccessStatusCode.CANCEL_LIKE_FEED,feedLike.isLike());
        }else{
            feedLike.clickLike();
            return new CustomResponseBody(SuccessStatusCode.LIKE_FEED,feedLike.isLike());
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
            String fileName = image.getImageUrl().replace("S3주소", "");
            awsS3Service.deleteFile(fileName);
        }
    }
    private Feed checkFeed(Long feedId){
        return feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
    //user 정보 가져오기
    private User checkUser(UserDetailsImpl userDetailsImpl) {
        return userRepository.findByLoginId(userDetailsImpl.getUsername()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
    }
}


