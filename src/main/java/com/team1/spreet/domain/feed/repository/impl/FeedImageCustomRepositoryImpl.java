package com.team1.spreet.domain.feed.repository.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.spreet.domain.feed.repository.FeedImageCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.team1.spreet.domain.feed.model.QFeed.feed;
import static com.team1.spreet.domain.feed.model.QFeedImage.feedImage;


@Repository
@RequiredArgsConstructor
public class FeedImageCustomRepositoryImpl implements FeedImageCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteByUserId(Long userId) {
        jpaQueryFactory
                .delete(feedImage)
                .where(feedImage.feed.id.in(
                        JPAExpressions
                                .select(feed.id)
                                .from(feed)
                                .where(feed.user.id.eq(userId))
                ))
                .execute();
    }
}
