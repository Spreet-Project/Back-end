package com.team1.spreet.domain.event.repository.impl;

import static com.team1.spreet.domain.event.model.QEventComment.eventComment;
import static com.team1.spreet.domain.user.model.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.event.dto.EventCommentDto;
import com.team1.spreet.domain.event.repository.EventCommentCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventCommentCustomRepositoryImpl implements EventCommentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<EventCommentDto.ResponseDto> findAllByEventId(Long eventId) {
		return jpaQueryFactory
			.select(Projections.fields(
				EventCommentDto.ResponseDto.class,
				eventComment.id.as("eventCommentId"),
				eventComment.user.nickname,
				eventComment.user.profileImage.as("profileImageUrl"),
				eventComment.content,
				eventComment.createdAt,
				eventComment.modifiedAt
			))
			.from(eventComment)
			.join(user)
			.on(eventComment.user.id.eq(user.id))
			.where(eventComment.event.id.eq(eventId), eventComment.deleted.eq(false))
			.orderBy(eventComment.createdAt.desc())
			.fetch();
	}

	@Override
	public void updateDeletedTrueByEventId(Long eventId) {
		jpaQueryFactory
			.update(eventComment)
			.set(eventComment.deleted, true)
			.where(eventComment.event.id.eq(eventId))
			.execute();
	}
}