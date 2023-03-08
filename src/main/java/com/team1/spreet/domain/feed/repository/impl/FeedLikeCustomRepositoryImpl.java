package com.team1.spreet.domain.feed.repository.impl;

import static com.team1.spreet.domain.feed.model.QFeed.feed;
import static com.team1.spreet.domain.feed.model.QFeedLike.feedLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.repository.FeedLikeCustomRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedLikeCustomRepositoryImpl implements FeedLikeCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MyPageDto.PostResponseDto> findAllByUserId(Long page, Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				MyPageDto.PostResponseDto.class,
				feedLike.feed.id,
				feedLike.feed.title,
				feedLike.feed.createdAt
			))
			.from(feedLike)
			.join(feedLike.feed, feed)
			.where(feedLike.user.id.eq(userId))
			.orderBy(feedLike.feed.createdAt.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}
}
