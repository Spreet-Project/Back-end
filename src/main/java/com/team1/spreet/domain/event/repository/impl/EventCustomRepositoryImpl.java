package com.team1.spreet.domain.event.repository.impl;

import static com.team1.spreet.domain.event.model.QEvent.event;
import static com.team1.spreet.domain.user.model.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.domain.event.model.AreaCode;
import com.team1.spreet.domain.event.repository.EventCustomRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
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
			.join(event.user, user)
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
			.join(event.user, user)
			.where(event.id.eq(eventId), event.deleted.eq(false))
			.fetchOne();
	}

	@Override
	public List<MyPageDto.PostResponseDto> findAllByUserId(String classification, Long page, Long userId) {
		return jpaQueryFactory
			.select(Projections.fields(
				MyPageDto.PostResponseDto.class,
				event.id,
				event.title,
				event.createdAt
			))
			.from(event)
			.where(event.user.id.eq(userId), event.deleted.eq(false))
			.orderBy(event.createdAt.desc())
			.offset(page * 10)
			.limit(10)
			.fetch();
	}

	@Override
	public List<EventDto.ResponseDto> findAllByAreaCode(AreaCode areaCode) {
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
			.join(event.user, user)
			.where(event.areaCode.eq(areaCode), event.deleted.eq(false))
			.orderBy(event.date.asc(), event.time.asc())    // 공연 일자가 빠른순으로 정렬
			.fetch();
	}

	@Override
	public void updateDeletedTrueByUserId(Long userId) {
		jpaQueryFactory
				.update(event)
				.set(event.deleted, true)
				.where(event.user.id.eq(userId))
				.execute();
	}

	@Override
	public List<EventDto.ResponseDto> findAllByDate(String startDate, String endDate) {
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
			.join(event.user, user)
			.where(event.date.between(startDate, endDate), event.deleted.eq(false))
			.orderBy(event.date.asc(), event.time.asc())
			.fetch();
	}
}
