package com.team1.spreet.domain.shorts.repository.impl;

import static com.team1.spreet.domain.shorts.model.QShortsComment.shortsComment;
import static com.team1.spreet.domain.user.model.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.shorts.dto.ShortsCommentDto;
import com.team1.spreet.domain.shorts.repository.ShortsCommentCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShortsCommentCustomRepositoryImpl implements ShortsCommentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ShortsCommentDto.ResponseDto> findAllByShortsId(Long shortsId) {
		return jpaQueryFactory
			.select(Projections.fields(
				ShortsCommentDto.ResponseDto.class,
				shortsComment.id.as("shortsCommentId"),
				shortsComment.user.nickname,
				shortsComment.content,
				shortsComment.user.profileImage.as("profileImageUrl"),
				shortsComment.createdAt,
				shortsComment.modifiedAt
			))
			.from(shortsComment)
			.join(shortsComment.user, user)
			.where(shortsComment.shorts.id.eq(shortsId), shortsComment.deleted.eq(false))
			.orderBy(shortsComment.createdAt.desc())
			.fetch();
	}

	@Override
	public void updateDeletedTrueByShortsId(Long shortsId) {
		jpaQueryFactory
			.update(shortsComment)
			.set(shortsComment.deleted, true)
			.where(shortsComment.shorts.id.eq(shortsId))
			.execute();
	}
}
