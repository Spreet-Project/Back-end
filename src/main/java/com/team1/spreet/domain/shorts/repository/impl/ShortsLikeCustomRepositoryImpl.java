package com.team1.spreet.domain.shorts.repository.impl;

import static com.team1.spreet.domain.shorts.model.QShorts.shorts;
import static com.team1.spreet.domain.shorts.model.QShortsLike.shortsLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.shorts.repository.ShortsLikeCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShortsLikeCustomRepositoryImpl implements ShortsLikeCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MyPageDto.PostResponseDto> findAllByUserId(Long page, Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				MyPageDto.PostResponseDto.class,
				shortsLike.shorts.id,
				shortsLike.shorts.title,
				shortsLike.shorts.category,
				shortsLike.shorts.createdAt
			))
			.from(shortsLike)
			.join(shortsLike.shorts, shorts)
			.where(shortsLike.user.id.eq(userId))
			.orderBy(shortsLike.shorts.createdAt.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}
}
