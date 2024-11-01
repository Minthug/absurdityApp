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

    Optional<Order> findByIdAndMember_Id(Long id, Long memberId);

    Optional<Order> findByUuidAndMember_Id(String uuid, Long memberId);

    @Query("SELECT o FROM Order o where o.createdAt <= :expiredTime AND o.status IN :statusList")
    List<Order> findByStatusInBeforeExpiredTime(@Param("expiredTime") LocalDateTime expiredTime,
                                                @Param("statusList") List<OrderStatus> statusList);

    Page<Order> findByMember_Id(Long memberId, PageRequest of);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o where o.id = :orderId")
    Optional<Order> findByIdPessimistic(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o where o.status = forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus.CHECK")
    Page<Order> findALlStatusInPayed(Pageable pageable);

    long countByMember_Id(Long memberId);

    @Query("""
    SELECT COALESCE(
        CAST(COUNT(CASE WHEN o.status = :completedStatus THEN 1 END) AS double) / 
        CAST(COUNT(o) AS double),
        0.0
    )
    FROM Order o
    WHERE o.member.id = :memberId
    """)
    double findCompletionRateByMemberId(@Param("memberId") Long memberId, @Param("completedStatus") List<OrderStatus> completedStatus);


    @Query("""
        SELECT COALESCE(AVG(oi.order.orderInfo.price), 0.0)
        FROM OrderItem oi
        WHERE oi.item.id = :itemId
        AND oi.order.status != 'OLDER_CANCEL'
        AND oi.order.status != 'YOUNGER_CANCEL'
""")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);

    @Query("""
        SELECT count(DISTINCT oi.order)
        FROM OrderItem oi
        WHERE oi.item.id = :itemId
""")
    Long countByItem_Id(Long itemId);
}
