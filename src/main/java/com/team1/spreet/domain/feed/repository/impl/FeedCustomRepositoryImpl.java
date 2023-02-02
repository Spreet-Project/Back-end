package com.team1.spreet.domain.feed.repository.impl;


import static com.team1.spreet.domain.feed.model.QFeed.feed;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.repository.FeedCustomRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

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
