package forOlderJava.absurdityAppForJava.domain.event.repository;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT e FROM Event e " +
    "LEFT JOIN FETCH e.eventItems ei " +
    "WHERE e.id = :eventId")
    Optional<Event> findByIdWithEventItems(@Param("eventId") Long eventId);
}
