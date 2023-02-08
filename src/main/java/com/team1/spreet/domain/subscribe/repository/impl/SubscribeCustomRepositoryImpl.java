package com.team1.spreet.domain.subscribe.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.subscribe.repository.SubscribeCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.team1.spreet.domain.subscribe.model.QSubscribe.subscribe;

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
}
