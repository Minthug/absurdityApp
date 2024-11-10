package forOlderJava.absurdityAppForJava.domain.younger.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ErrandRepository extends JpaRepository<Errand, Long> {
    boolean existsByOrder(Order order);

    @Query("SELECT e from Errand e join fetch e.order where e.order.id = :orderId")
    Optional<Errand> findByOrderIdWithOrder(@Param("orderId") final Long orderId);



    @Query(value = "select e from Errand e"
                   + " where e.errandStatus"
    + " = forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus.ACCEPTING_ERRAND"
    + " and e.younger is null ")
    Page<Errand> findWaitingErrands(Pageable pageable);


    @Query(value = "select e from Errand e"
    + " where e.younger = :younger and e.errandStatus in :status")
    Page<Errand> findYoungerErrand(@Param("errand") Errand errand,
                                   @Param("status") ErrandStatus errandStatus,
                                   Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select e from Errand e where e.id = :errandId")
    Optional<Errand> findByIdOptimistic(@Param("errandId") Long errandId);

    @Query("select e from Errand e join e.order o where o.member = :member")
    List<Errand> findAllByMember(@Param("member") Member member);

    @Query("select e from Errand e"
    + " join fetch e.order o"
    + " join fetch o.orderItems oi"
    + " join fetch oi.item i"
    + " where e.id = :errandId")
    Optional<Errand> findByIdWithOrderAndItems(@Param("errandId") Long errandId);

}
