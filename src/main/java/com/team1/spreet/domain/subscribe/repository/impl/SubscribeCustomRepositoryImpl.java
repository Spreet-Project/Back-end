package com.team1.spreet.domain.subscribe.repository.impl;

import static com.team1.spreet.domain.subscribe.model.QSubscribe.subscribe;
import static com.team1.spreet.domain.user.model.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.subscribe.repository.SubscribeCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscribeCustomRepositoryImpl implements SubscribeCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public void deleteByUserId(Long userId) {
		jpaQueryFactory
			.delete(subscribe)
			.where(subscribe.subscriber.id.eq(userId).or(subscribe.publisher.id.eq(userId)))
			.execute();
	}

	@Override
	public List<MyPageDto.SubscribeInfoDto> findAllBySubscriberId(Long page, Long subscriberId) {
		return jpaQueryFactory
			.select(Projections.fields(
				MyPageDto.SubscribeInfoDto.class,
				subscribe.publisher.id.as("userId"),
				subscribe.publisher.nickname
			))
			.from(subscribe)
			.join(subscribe.publisher, user)
			.where(subscribe.subscriber.id.eq(subscriberId))
			.offset(page * 10)
			.limit(10)
			.fetch();
	}

}
