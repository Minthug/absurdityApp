package forOlderJava.absurdityAppForJava.domain.item.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.ItemSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static forOlderJava.absurdityAppForJava.domain.item.QItem.item;
import static forOlderJava.absurdityAppForJava.domain.order.entity.QOrderItem.orderItem;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final int NEW_PRODUCT_REFERENCE_TIME = 2;
    private static final double HOT_PRODUCT_REFERENCE_RATE = 3.7;
    private static final int HOT_PRODUCT_REFERENCE_ORDERS = 10;

    @Override
    public List<Item> findNewItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable) {
        OrderSpecifier orderSpecifier = createOrderSpecifier(itemSortType);
        Predicate predicate = item.createdAt.after(LocalDateTime.now().minus(NEW_PRODUCT_REFERENCE_TIME, ChronoUnit.WEEKS));

        return queryFactory
                .selectFrom(item)
                .leftJoin(item.orderItems, orderItem)
                .where(predicate)
                .groupBy(item)
                .having(getHavingCondition(lastIdx, lastItemId, itemSortType))
                .orderBy(orderSpecifier, item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier createOrderSpecifier(ItemSortType itemSortType) {
        return switch (itemSortType) {
            case NEW -> new OrderSpecifier<>(Order.DESC, item.id);
            case HIGHEST_AMOUNT -> new OrderSpecifier<>(Order.DESC, item.price);
            case LOWEST_AMOUNT -> new OrderSpecifier<>(Order.ASC, item.price);
            case DISCOUNT -> new OrderSpecifier<>(Order.DESC, item.discount);
            default -> new OrderSpecifier<>(Order.DESC, item.orderItems.any().quantity.sum());
        };
    }

    @Override
    public List<Item> findHotItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable) {
        OrderSpecifier orderSpecifier = createOrderSpecifier(itemSortType);
        Predicate predicate = item.rate.gt(HOT_PRODUCT_REFERENCE_RATE);
        Predicate orderCondition = JPAExpressions.select(orderItem.quantity.sum().coalesce(0))
                .from(orderItem)
                .where(orderItem.item.eq(item))
                .gt(HOT_PRODUCT_REFERENCE_ORDERS);

        return queryFactory
                .selectFrom(item)
                .leftJoin(item.orderItems, orderItem)
                .where(predicate)
                .groupBy(item)
                .having(getHavingCondition(lastIdx, lastItemId, itemSortType), orderCondition)
                .orderBy(orderSpecifier, item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Predicate getHavingCondition(Long lastIdx, Long lastItemId, ItemSortType itemSortType) {
        return switch (itemSortType) {
            case NEW -> item.id.lt(lastIdx);
            case HIGHEST_AMOUNT -> item.price.lt(lastIdx)
                    .or(item.price.eq(lastIdx.intValue()).and(item.id.gt(lastItemId)));
            case LOWEST_AMOUNT -> item.price.gt(lastIdx)
                    .or(item.price.eq(lastIdx.intValue()).and(item.id.gt(lastItemId)));
            case DISCOUNT -> item.discount.lt(lastIdx)
                    .or(item.discount.eq(lastIdx.intValue()).and(item.id.gt(lastItemId)));
            default -> JPAExpressions.select(orderItem.quantity.longValue().sum().coalesce(0L))
                    .from(orderItem)
                    .where(orderItem.item.eq(item))
                    .lt(lastIdx);
        };
    }
}
