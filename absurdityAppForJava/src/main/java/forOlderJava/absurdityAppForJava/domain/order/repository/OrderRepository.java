package forOlderJava.absurdityAppForJava.domain.order.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    void deleteByMember(Member member);

    Optional<Order> findByOrderIdAndMember_MemberId(Long orderId, Long memberId);

    Optional<Order> findByUuidAndMember_MemberId(String uuid, Long memberId);

    @Query("SELECT o FROM Order o where o.createdAt <= :expiredTime AND o.status IN :statusList")
    List<Order> findByStatusInBeforeExpiredTime(@Param("expiredTime") LocalDateTime expiredTime,@Param("statusList") List<OrderStatus> statusList);

    Page<Order> findByMember_MemberId(Long memberId, PageRequest of);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o where o.id = :orderId")
    Optional<Order> findByIdPessimistic(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o where o.status = forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus.CHECK")
    Page<Order> findALlStatusInPayed(Pageable pageable);
}
