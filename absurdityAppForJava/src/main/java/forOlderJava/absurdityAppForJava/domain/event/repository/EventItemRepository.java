package forOlderJava.absurdityAppForJava.domain.event.repository;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.EventItem;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventItemRepository extends JpaRepository<EventItem, Long> {

    boolean existsByEventAndItem(Event event, Item item);

    @Query("SELECT ei.item FROM EventItem ei WHERE ei.event = :event AND ei.item IN :items")
    List<Item> findDuplicatedItems(@Param("event") Event event,
                                   @Param("item") List<Item> items);
}
