package forOlderJava.absurdityAppForJava.domain.order.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    void deleteByMember(Member member);

    Optional<Order> findByOrderIdAndMember_MemberId(Long orderId, Long memberId);
}
