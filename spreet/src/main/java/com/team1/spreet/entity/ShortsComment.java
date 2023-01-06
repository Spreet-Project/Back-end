package com.team1.spreet.entity;

import com.team1.spreet.dto.ShortsCommentDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE shorts_comment SET is_deleted = true WHERE shorts_comment_id = ?")
@Where(clause = "is_deleted = false")
public class ShortsComment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHORTS_COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHORTS_ID")
    private Shorts shorts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public ShortsComment(ShortsCommentDto.RequestDto requestDto, Shorts shorts, User user) {
        this.content = requestDto.getContent();
        this.shorts = shorts;
        this.user = user;
    }

    public void updateShortsComment(ShortsCommentDto.RequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
