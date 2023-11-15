package com.fc.toy_project3.domain.like.repository;

import static com.fc.toy_project3.domain.like.entity.QLike.like;
import static com.fc.toy_project3.domain.trip.entity.QTrip.trip;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.global.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LikeCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Like> findAllByMemberIdAndPageable(long memberId, Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = getAllOrderSpecifiers(pageable);
        List<Like> content = jpaQueryFactory
            .selectFrom(like)
            .leftJoin(like.member)
            .leftJoin(like.trip)
            .fetchJoin()
            .where(like.member.id.eq(memberId).and(like.trip.deletedAt.isNull()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
            .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(like.count())
            .from(like)
            .leftJoin(like.member)
            .leftJoin(like.trip)
            .where(like.member.id.eq(memberId).and(like.trip.deletedAt.isNull()));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction =
                    order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "likeCount":
                        OrderSpecifier<?> orderLikeCount = QueryDslUtil.getSortedColumn(direction,
                            trip, "likeCount");
                        orderSpecifiers.add(orderLikeCount);
                        break;
                    default:
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction,
                            trip, "createdAt");
                        orderSpecifiers.add(orderCreatedAt);
                }
            }
        }
        return orderSpecifiers;
    }
}
