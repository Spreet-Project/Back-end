package com.team1.spreet.domain.feed.dto;

import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class FeedDto {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDto {
        @ApiModelProperty(value = "제목", required = true)
        @NotBlank(message = "제목을 입력해 주세요.")
        private String title;

        @ApiModelProperty(value = "게시글 내용", required = true)
        @NotBlank(message = "내용을 입력해 주세요.")
        private String content;

        @ApiModelProperty(value = "이미지 파일")
        private List<MultipartFile> file;

        public Feed toEntity(String title, String content, User user){
            return new Feed(title, content, user);
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseDto {
        @ApiModelProperty(value = "피드 ID")
        private Long feedId;

        @ApiModelProperty(value = "닉네임")
        private String nickname;

        @ApiModelProperty(value = "제목")
        private String title;

        @ApiModelProperty(value = "게시글 내용")
        private String content;

        @ApiModelProperty(value = "이미지 url 리스트")
        private List<String> imageUrlList;

        @ApiModelProperty(value = "프로필 이미지")
        private String profileImageUrl;

        @ApiModelProperty(value = "좋아요 개수")
        private Long likeCount;

        @ApiModelProperty(value = "좋아요 상태")
        private boolean liked;

        @ApiModelProperty(value = "구독 유무")
        private boolean subscribed;

        public void addImageUrlList(List<String> imageUrlList){
            this.imageUrlList = imageUrlList;
        }
    }
    @NoArgsConstructor
    @Getter
    public static class SimpleResponseDto implements Serializable {

        @ApiModelProperty(value = "피드 ID")
        private Long feedId;

        @ApiModelProperty(value = "제목")
        private String title;
    }
}
