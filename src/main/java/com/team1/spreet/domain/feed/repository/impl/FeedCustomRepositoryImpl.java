package com.team1.spreet.domain.feed.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.dto.FeedDto;
import com.team1.spreet.domain.feed.repository.FeedCustomRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.team1.spreet.domain.feed.model.QFeed.feed;
import static com.team1.spreet.domain.feed.model.QFeedLike.feedLike;
import static com.team1.spreet.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public FeedDto.ResponseDto findByIdAndUserId(Long feedId, Long userId){
        return jpaQueryFactory
                .select(Projections
                        .fields(FeedDto.ResponseDto.class,
                                feed.id.as("feedId"),
                                feed.user.nickname,
                                feed.title,
                                feed.content,
                                feed.user.profileImage,
                                ExpressionUtils.as(
                                        JPAExpressions.select(feedLike.count())
                                                .from(feedLike)
                                                .where(feedLike.feed.id.eq(feed.id)),
                                        "feedLike"
                                ),
                                ExpressionUtils.as(
                                        JPAExpressions.select(feedLike.user.id.isNotNull())
                                                .from(feedLike)
                                                .where(feedLike.feed.id.eq(feed.id), feedLike.user.id.eq(userId)),
                                        "liked"
                                )
                        )
                )
                .from(feed)
                .join(user)
                .on(feed.user.id.eq(user.id))
                .where(feed.id.eq(feedId), feed.deleted.eq(false))
                .fetchOne();
    }
    @Override
    public List<FeedDto.ResponseDto> findByOrderByCreatedAtDesc(Long page, Long size, Long userId){
        return jpaQueryFactory
                .select(Projections
                    .fields(FeedDto.ResponseDto.class,
                        feed.id.as("feedId"),
                        feed.user.nickname,
                        feed.title,
                        feed.content,
                        feed.user.profileImage,
                        ExpressionUtils.as(
                                JPAExpressions.select(feedLike.count())
                                        .from(feedLike)
                                        .where(feedLike.feed.id.eq(feed.id)),
                                "feedLike"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(feedLike.user.id.isNotNull())
                                        .from(feedLike)
                                        .where(feedLike.feed.id.eq(feed.id), feedLike.user.id.eq(userId)),
                                "liked"
                        )
                    )
                )
                .from(feed)
                .join(user)
                .on(feed.user.id.eq(user.id))
                .where(feed.deleted.eq(false))
                .orderBy(feed.createdAt.desc())
                .offset(page*10)
                .limit(size)
                .fetch();
    }
    @Override
    public List<FeedDto.SimpleResponseDto> getSimpleFeed() {
        return jpaQueryFactory
                .select(Projections
                        .fields(FeedDto.SimpleResponseDto.class,
                                feed.id.as("feedId"),
                                feed.title
                        )
                )
                .from(feed)
                .where(feed.deleted.eq(false))
                .orderBy(feed.createdAt.desc())
                .offset(0)
                .limit(10)
                .fetch();
    }
	@Override
	public List<MyPageDto.PostResponseDto> findByUserId(String classification, Long page, Long userId) {
		return jpaQueryFactory
				.select(Projections.constructor(
						MyPageDto.PostResponseDto.class,
						ExpressionUtils.toExpression(classification),
						feed.id,
						feed.title,
						feed.createdAt
				))
				.from(feed)
				.where(feed.user.id.eq(userId), feed.deleted.eq(false))
				.orderBy(feed.createdAt.desc())
				.offset(page * 10)
				.limit(10)
				.fetch();
	}
}
