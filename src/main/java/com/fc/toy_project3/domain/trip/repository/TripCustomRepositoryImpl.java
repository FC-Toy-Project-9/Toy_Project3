package com.fc.toy_project3.domain.trip.repository;

import static com.fc.toy_project3.domain.trip.entity.QTrip.trip;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.QueryDslUtil;
import com.querydsl.core.BooleanBuilder;
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
import org.springframework.stereotype.Repository;

@Repository
public class TripCustomRepositoryImpl implements TripCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public TripCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Trip> findAllBySearchCondition(GetTripsRequestDTO getTripsRequestDTO,
        Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = getAllOrderSpecifiers(pageable);
        List<Trip> content = jpaQueryFactory
            .selectFrom(trip)
            .leftJoin(trip.member)
            .fetchJoin()
            .where(createSearchConditionsBuilder(getTripsRequestDTO))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
            .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(trip.count())
            .from(trip)
            .leftJoin(trip.member)
            .where(createSearchConditionsBuilder(getTripsRequestDTO));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder createSearchConditionsBuilder(
        GetTripsRequestDTO getTripsRequestDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        boolean isFindAllByTripName = getTripsRequestDTO.getTripName() != null;
        boolean isFindAllByNickname = getTripsRequestDTO.getNickname() != null;
        if (isFindAllByTripName) {
            booleanBuilder.and(trip.name.contains(getTripsRequestDTO.getTripName()));
        }
        if (isFindAllByNickname) {
            booleanBuilder.and(
                trip.member.nickname.contains(getTripsRequestDTO.getNickname()));
        }
        booleanBuilder.and(trip.deletedAt.isNull());
        return booleanBuilder;
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
