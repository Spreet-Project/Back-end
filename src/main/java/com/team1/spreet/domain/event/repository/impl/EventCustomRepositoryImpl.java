package com.team1.spreet.domain.event.repository.impl;

import static com.team1.spreet.domain.event.model.QEvent.event;
import static com.team1.spreet.domain.user.model.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.domain.event.repository.EventCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<EventDto.ResponseDto> findAllSortByNew() {
		return jpaQueryFactory
			.select(Projections.fields(
				EventDto.ResponseDto.class,
				event.id.as("eventId"),
				event.title,
				event.content,
				event.location,
				event.date,
				event.time,
				event.eventImageUrl,
				event.user.nickname,
				event.user.profileImage.as("profileImageUrl")
			))
			.from(event)
			.join(user)
			.on(event.user.id.eq(user.id))
			.where(event.deleted.eq(false))
			.orderBy(event.createdAt.desc(), event.id.desc())
			.fetch();
	}

	@Override
	public EventDto.ResponseDto findByEventId(Long eventId) {
		return jpaQueryFactory
			.select(Projections.fields(
				EventDto.ResponseDto.class,
				event.id.as("eventId"),
				event.title,
				event.content,
				event.location,
				event.date,
				event.time,
				event.eventImageUrl,
				event.user.nickname,
				event.user.profileImage.as("profileImageUrl")
			))
			.from(event)
			.join(user)
			.on(event.user.id.eq(user.id))
			.where(event.id.eq(eventId), event.deleted.eq(false))
			.fetchOne();
	}

}
