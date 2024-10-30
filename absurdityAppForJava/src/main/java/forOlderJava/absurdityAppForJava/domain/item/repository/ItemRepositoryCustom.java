package forOlderJava.absurdityAppForJava.domain.item.repository;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.ItemSortType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> findNewItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable);

    List<Item> findHotItemsOrderBy(Long lastIdx, Long lastItemId, ItemSortType itemSortType, Pageable pageable);

}
