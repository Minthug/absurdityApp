//package forOlderJava.absurdityAppForJava.domain.item.repository;
//
//import com.querydsl.core.types.OrderSpecifier;
//import com.querydsl.core.types.Predicate;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import forOlderJava.absurdityAppForJava.domain.item.Item;
//import forOlderJava.absurdityAppForJava.domain.item.ItemSortType;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class ItemRepositoryImpl implements ItemRepositoryCustom {
//
//    private final JPAQueryFactory queryFactory;
//    private static final int NEW_PRODUCT_REFERENCE_TIME = 2;
//    private static final double HOT_PRODUCT_REFERENCE_RATE = 3.7;
//    private static final int HOT_PRODUCT_REFERENCE_ORDERS = 10;
//
//    @Override
//    public List<Item> findNewItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable) {
//        OrderSpecifier orderSpecifier = createOrderSpecifier(itemSortType);
//        Predicate predicate = item.createdAt.after(LocalDateTime.now().minus(NEW_PRODUCT_REFERENCE_TIME, ChronoUnit.WEEKS));
//
//        return queryFactory
//                .selectFrom(item)
//    }
//
//    @Override
//    public List<Item> findHotItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable) {
//        return List.of();
//    }
//}
