package forOlderJava.absurdityAppForJava.domain.order.repository;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}