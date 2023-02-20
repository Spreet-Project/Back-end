package com.team1.spreet.domain.feed.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.dto.FeedDto;
import com.team1.spreet.domain.feed.repository.FeedCustomRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.team1.spreet.domain.feed.model.QFeed.feed;
import static com.team1.spreet.domain.feed.model.QFeedLike.feedLike;
import static com.team1.spreet.domain.subscribe.model.QSubscribe.subscribe;
import static com.team1.spreet.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public FeedDto.ResponseDto findAllByIdAndUserId(Long feedId, Long userId){
        return jpaQueryFactory
                .select(Projections
                        .fields(FeedDto.ResponseDto.class,
                                feed.id.as("feedId"),
                                feed.user.nickname,
                                feed.title,
                                feed.content,
                                feed.user.profileImage.as("profileImageUrl"),
                                feed.likeCount,
//                                ExpressionUtils.list(List.class,
//                                        select(feedImage.imageUrl)
//                                                .from(feedImage)
//                                                .where(feedImage.feed.id.eq(feed.id))
//                                ),
                                ExpressionUtils.as(
                                        select(feedLike.user.id.isNotNull())
                                                .from(feedLike)
                                                .where(feedLike.feed.id.eq(feed.id), feedLike.user.id.eq(userId)),
                                        "liked"
                                ),
                                ExpressionUtils.as(
                                        select(subscribe.id.isNotNull())
                                                .from(subscribe)
                                                .where(subscribe.publisher.id.eq(feed.user.id), subscribe.subscriber.id.eq(userId)),
                                        "subscribed"
                                )
                        )
                )
                .from(feed)
                .join(feed.user, user)
                .on(feed.user.id.eq(user.id))
                .where(feed.id.eq(feedId), feed.deleted.eq(false))
                .fetchOne();
    }
    @Override
    public List<FeedDto.ResponseDto> findAllSortBy(String sort, Long page, Long size, Long userId, String searchType, String searchKeyword){
        OrderSpecifier sorting = sort.equals("popular") ? feed.likeCount.desc() : feed.createdAt.desc();
        return jpaQueryFactory
                .select(Projections
                    .fields(FeedDto.ResponseDto.class,
                        feed.id.as("feedId"),
                        feed.user.nickname,
                        feed.title,
                        feed.content,
                        feed.user.profileImage.as("profileImageUrl"),
                        feed.likeCount,
                        ExpressionUtils.as(
                                select(feedLike.user.id.isNotNull())
                                        .from(feedLike)
                                        .where(feedLike.feed.id.eq(feed.id), feedLike.user.id.eq(userId)),
                                "liked"
                        ),
                            ExpressionUtils.as(
                                select(subscribe.id.isNotNull())
                                        .from(subscribe)
                                        .where(subscribe.publisher.id.eq(feed.user.id), subscribe.subscriber.id.eq(userId)),
                               "subscribed"
                            )
                    )
                )
                .from(feed)
                .join(feed.user, user)
                .where(feed.deleted.eq(false), searchCondition(searchType, searchKeyword))
                .orderBy(sorting)
                .offset(page*10)
                .limit(size)
                .fetch();
    }
    private BooleanExpression searchCondition(String searchType, String searchKeyword) {
        if(searchType.equals("title")) return feed.title.contains(searchKeyword);
        if(searchType.equals("content")) return feed.content.contains(searchKeyword);
        return null;
    }
    @Override
    public List<FeedDto.SimpleResponseDto> findMainFeed() {
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
	public List<MyPageDto.PostResponseDto> findAllByUserId(String classification, Long page, Long userId) {
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

    @Override
    public void updateDeletedTrueByUserId(Long userId) {
        jpaQueryFactory
                .update(feed)
                .set(feed.deleted, true)
                .where(feed.user.id.eq(userId))
                .execute();
    }
}
