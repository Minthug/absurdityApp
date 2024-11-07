package forOlderJava.absurdityAppForJava.domain.younger.repository;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrandRepository extends JpaRepository<Errand, Long> {
    boolean existsByOrder(Order order);
}
