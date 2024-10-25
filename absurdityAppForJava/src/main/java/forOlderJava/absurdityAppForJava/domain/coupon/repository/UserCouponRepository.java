package forOlderJava.absurdityAppForJava.domain.coupon.repository;

import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    void deleteByMember(Member findMember);

    Optional<UserCoupon> findByIdWithCoupon(Long id);
}
