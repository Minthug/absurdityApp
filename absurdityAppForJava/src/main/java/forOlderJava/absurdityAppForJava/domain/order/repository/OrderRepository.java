package forOlderJava.absurdityAppForJava.domain.order.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    void deleteByMember(Member member);

    Optional<Order> findByOrderIdAndMember_MemberId(Long orderId, Long memberId);

    List<Order> findByStatusInBeforeExpiredTime(LocalDateTime expiredTime, List<OrderStatus> statusList);

    Page<Order> findByMember_MemberId(Long memberId, PageRequest of);
}
