package forOlderJava.absurdityAppForJava.domain.item.repository;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id = :itemId")
    Optional<Item> findByItemId(@Param("itemId") Long itemId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Item i set i.quantity = i.quantity + :quantity where i.id = :id")
    void increaseQuantity(Long id, Integer quantity);

    @Query("select i from Item i where i.id in ?1")
    List<Item> findByItemIdIn(Collection<Long> itemIds);

//    findByItemIdIN의 개선된 버전
//    @Query("select i from Item i where i.id in ?1 order by i.id")
//    List<Item> findByItemIdInOrderById(Collection<Long> itemIds);
}
