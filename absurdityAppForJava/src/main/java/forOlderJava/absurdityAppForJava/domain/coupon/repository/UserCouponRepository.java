package forOlderJava.absurdityAppForJava.domain.coupon.repository;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("SELECT uc from UserCoupon uc join fetch uc.coupon c where uc.id = :id")
    Optional<UserCoupon> findByIdWithCoupon(Long id);

    boolean existsByMemberAndCoupon(Member member, Coupon coupon);

    @Query("SELECT uc from UserCoupon uc JOIN FETCH uc.coupon c "
    + "where uc.member = :member and uc.isUsed = :isUsed AND c.endAt >= :currentDate")
    List<UserCoupon> findByMemberAndIsUsedAndCouponEndAtAfter(@Param("member") Member member,
                                                              @Param("isUsed") boolean isUsed,
                                                              @Param("currentDate")LocalDate currentDate);

    void deleteByMember(Member findMember);
}
