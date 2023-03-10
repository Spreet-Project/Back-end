package com.team1.spreet.domain.shorts.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.shorts.dto.ShortsDto;
import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.repository.ShortsCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.team1.spreet.domain.shorts.model.QShorts.shorts;
import static com.team1.spreet.domain.shorts.model.QShortsLike.shortsLike;
import static com.team1.spreet.domain.subscribe.model.QSubscribe.subscribe;
import static com.team1.spreet.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class ShortsCustomRepositoryImpl implements ShortsCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ShortsDto.MainResponseDto> findMainShortsByCategory(Category category) {
		return jpaQueryFactory
			.select(Projections.fields(
				ShortsDto.MainResponseDto.class,
				shorts.id.as("shortsId"),
				shorts.user.nickname,
				shorts.title,
				shorts.videoUrl,
				shorts.category,
				shorts.user.profileImage.as("profileImageUrl")
			))
			.from(shorts)
			.join(shorts.user, user)
			.where(shorts.category.eq(category), shorts.deleted.eq(false))
			.orderBy(shorts.likeCount.desc(), shorts.id.desc())
			.limit(10)
			.fetch();
	}

	@Override
	public List<ShortsDto.ResponseDto> findAllSortByNewAndCategory(Category category, Long page,
		Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				ShortsDto.ResponseDto.class,
				shorts.id.as("shortsId"),
				shorts.user.nickname,
				shorts.title,
				shorts.content,
				shorts.videoUrl,
				shorts.category,
				shorts.user.profileImage.as("profileImageUrl"),
				shorts.likeCount,
				// 로그인 유저의 해당 shorts 좋아요 유무
				ExpressionUtils.as(
					select(shortsLike.user.id.isNotNull())
						.from(shortsLike)
						.where(shortsLike.shorts.eq(shorts), shortsLike.user.id.eq(userId)),
					"liked"),
				// 로그인 유저의 해당 유저 구독 유무
				ExpressionUtils.as(
					select(subscribe.subscriber.id.isNotNull())
						.from(subscribe)
						.where(subscribe.publisher.id.eq(shorts.user.id), subscribe.subscriber.id.eq(userId)),
					"subscribed"))
			)
			.from(shorts)
			.join(shorts.user, user)
			.where(shorts.category.eq(category), shorts.deleted.eq(false))
			.orderBy(shorts.createdAt.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}

	@Override
	public List<ShortsDto.ResponseDto> findAllSortByPopularAndCategory(Category category, Long page,
		Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				ShortsDto.ResponseDto.class,
				shorts.id.as("shortsId"),
				shorts.user.nickname,
				shorts.title,
				shorts.content,
				shorts.videoUrl,
				shorts.category,
				shorts.user.profileImage.as("profileImageUrl"),
				shorts.likeCount,
				// 로그인 유저의 해당 shorts 좋아요 유무
				ExpressionUtils.as(
					select(shortsLike.user.id.isNotNull())
						.from(shortsLike)
						.where(shortsLike.shorts.eq(shorts), shortsLike.user.id.eq(userId)), "liked"),
				// 로그인 유저의 해당 유저 구독 유무
				ExpressionUtils.as(
					select(subscribe.subscriber.id.isNotNull())
						.from(subscribe)
						.where(subscribe.publisher.id.eq(shorts.user.id), subscribe.subscriber.id.eq(userId)),
					"subscribed"))
			)
			.from(shorts)
			.join(shorts.user, user)
			.where(shorts.category.eq(category), shorts.deleted.eq(false))
			.orderBy(shorts.likeCount.desc(), shorts.id.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}

	@Override
	public ShortsDto.ResponseDto findByIdAndUserId(Long shortsId, Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				ShortsDto.ResponseDto.class,
				shorts.id.as("shortsId"),
				shorts.user.nickname,
				shorts.title,
				shorts.content,
				shorts.videoUrl,
				shorts.category,
				shorts.user.profileImage.as("profileImageUrl"),
				shorts.likeCount,
				// 로그인 유저의 해당 shorts 좋아요 유무
				ExpressionUtils.as(
					select(shortsLike.user.id.isNotNull())
						.from(shortsLike)
						.where(shortsLike.shorts.eq(shorts), shortsLike.user.id.eq(userId)), "liked"),
				// 로그인 유저의 해당 유저 구독 유무
				ExpressionUtils.as(
					select(subscribe.subscriber.id.isNotNull())
						.from(subscribe)
						.where(subscribe.publisher.id.eq(shorts.user.id), subscribe.subscriber.id.eq(userId)),
					"subscribed"))
			)
			.from(shorts)
			.join(shorts.user, user)
			.where(shorts.id.eq(shortsId), shorts.deleted.eq(false))
			.fetchOne();
	}

	@Override
	public List<MyPageDto.PostResponseDto> findAllByUserId(String classification, Long page, Long userId) {
		return jpaQueryFactory
			.select(Projections.constructor(
				MyPageDto.PostResponseDto.class,
				ExpressionUtils.toExpression(classification),
				shorts.id,
				shorts.title,
				shorts.category,
				shorts.createdAt
			))
			.from(shorts)
			.where(shorts.user.id.eq(userId), shorts.deleted.eq(false))
			.orderBy(shorts.createdAt.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}

	@Override
	public void updateDeletedTrueByUserId(Long userId){
		jpaQueryFactory
				.update(shorts)
				.set(shorts.deleted, true)
				.where(shorts.user.id.eq(userId))
				.execute();
	}

}
