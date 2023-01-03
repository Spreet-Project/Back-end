package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedComment;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.FeedCommentRepository;
import com.team1.spreet.repository.FeedLikeRepository;
import com.team1.spreet.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final AwsS3Service awsS3Service;
    private final FeedCommentRepository feedCommentRepository;

    @Transactional
    public CustomResponseBody<SuccessStatusCode> saveFeed(FeedDto.RequestDto requestDto, User user) {  //user는 userDetail로 변경 예정
        Feed feed = new Feed(requestDto.getTitle(), requestDto.getContent(), user);
        feedRepository.save(feed);  //feed 저장
        //다수 image 저장
        List<String> imageList = new ArrayList<>();
        List<MultipartFile> multipartFileList = requestDto.getMultipartFileList();
        for (MultipartFile multipartFile : multipartFileList) {
            imageList.add(awsS3Service.uploadFile(multipartFile));
        }
        /*image 엔티티에 저장하는 로직 들어갈 자리
         *
         * */
        Long feedLike = feedLikeRepository.countByFeedIdAndIsLikeTrue(feed.getId());    //좋아요 개수 가져오기
        boolean isLike = feedLikeRepository.existsByUserIdAndFeedId(user.getId(), feed.getId());    //좋아요 체크 여부 확인
        return new CustomResponseBody<SuccessStatusCode>(SuccessStatusCode.SAVE_FEED);
    }
    @Transactional
    public CustomResponseBody<SuccessStatusCode> updateFeed(Long feedId, FeedDto.RequestDto requestDto, User user) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
        feed.update(requestDto.getTitle(), requestDto.getContent(), user);    //feed 내용 수정
        /*기존에 업로드된 이미지 제거
        List<String> imageUrlList = imageRepository.findByFeedId(feedId);
        for (String s : imageUrlList) {
            String fileName = s.replace("S3주소", "");
            awsS3Service.deleteFile(fileName);
        }
        */
        /*새로운 이미지 저장
        List<MultipartFile> multipartFileList = requestDto.getMultipartFileList();
        for (MultipartFile multipartFile : multipartFileList) {
            String imageUrl = awsS3Service.uploadFile(multipartFile);
            Image image = new Image(feedId, imageUrl);
            imageRepository.save(image);
        }
        */
        return new CustomResponseBody<SuccessStatusCode>(SuccessStatusCode.UPDATE_FEED);
    }

    public CustomResponseBody<SuccessStatusCode> deleteFeed(Long feedId, User user) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
        feed.setDeleted();  //feed delete
        //comment delete
        List<FeedComment> feedCommentList = feedCommentRepository.findAllByFeedId(feedId);
        for (FeedComment feedComment : feedCommentList) {
            feedComment.setDeleted();
        }
        return new CustomResponseBody<SuccessStatusCode>(SuccessStatusCode.DELETE_FEED);
    }
}
