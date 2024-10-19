package forOlderJava.absurdityAppForJava.domain.order.repository;

import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByItem();
}
