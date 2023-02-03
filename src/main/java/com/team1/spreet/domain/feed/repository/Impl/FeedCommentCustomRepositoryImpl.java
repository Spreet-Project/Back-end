package com.team1.spreet.domain.feed.repository.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.dto.FeedCommentDto;
import com.team1.spreet.domain.feed.repository.FeedCommentCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.team1.spreet.domain.feed.model.QFeedComment.feedComment;
import static com.team1.spreet.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class FeedCommentCustomRepositoryImpl implements FeedCommentCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<FeedCommentDto.ResponseDto> findAllByFeedId(Long feedId){
        return jpaQueryFactory
                .select(Projections.fields(
                                FeedCommentDto.ResponseDto.class,
                                feedComment.id.as("commentId"),
                                feedComment.user.nickname,
                                feedComment.content,
                                feedComment.user.profileImage.as("profileImageUrl"),
                                feedComment.createdAt,
                                feedComment.modifiedAt
                                )
                )
                .from(feedComment)
                .join(user)
                .on(feedComment.user.id.eq(user.id))
                .where(feedComment.feed.id.eq(feedId), feedComment.deleted.eq(false))
                .orderBy(feedComment.createdAt.desc())
                .fetch();
    }
    public void updateDeletedTrueByFeedId(Long feedId){
        jpaQueryFactory
                .update(feedComment)
                .set(feedComment.deleted, true)
                .where(feedComment.feed.id.eq(feedId))
                .execute();
    }
}
