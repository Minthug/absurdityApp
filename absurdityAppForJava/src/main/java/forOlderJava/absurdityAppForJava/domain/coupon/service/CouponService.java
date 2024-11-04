package forOlderJava.absurdityAppForJava.domain.coupon.service;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;
import forOlderJava.absurdityAppForJava.domain.coupon.exception.InvalidCouponException;
import forOlderJava.absurdityAppForJava.domain.coupon.exception.NotFoundCouponException;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.CouponRepository;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.UserCouponRepository;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterCouponCommand;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterUserCouponCommand;
import forOlderJava.absurdityAppForJava.domain.coupon.service.response.FindCouponsResponse;
import forOlderJava.absurdityAppForJava.domain.coupon.service.response.FindIssuedCouponsResponse;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createCoupon(RegisterCouponCommand registerCouponCommand) {
        Coupon coupon = Coupon.builder()
                .name(registerCouponCommand.name())
                .discount(registerCouponCommand.discount())
                .description(registerCouponCommand.description())
                .endAt(registerCouponCommand.endAt())
                .build();

        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public Long registerUserCoupon(RegisterUserCouponCommand command) {
        Member findMember = findMemberByMemberId(command.memberId());
        Coupon findCoupon = findCouponByCouponId(command.couponId());

        validateCouponExpiration(findCoupon.getEndAt());
        validateAlreadyIssuedCoupon(findMember, findCoupon);

        UserCoupon userCoupon = new UserCoupon(findMember, findCoupon);
        return userCouponRepository.save(userCoupon).getId(); // getId() == getUserCouponId();
    }

    @Transactional
    public FindCouponsResponse findCoupons() {

        List<Coupon> findCoupons = couponRepository.findByEndAtGreaterThanEqual(LocalDate.now());
        return FindCouponsResponse.from(findCoupons);
    }

    @Transactional(readOnly = true)
    public FindIssuedCouponsResponse findIssuedCoupons(Long memberId) {
        Member findMember = findMemberByMemberId(memberId);
        List<UserCoupon> findUserCoupons = userCouponRepository.findByMemberAndIsUsedAndCouponEndAtAfter
                (findMember, false, LocalDate.now());

        return FindIssuedCouponsResponse.from(findUserCoupons);
    }

    private void validateAlreadyIssuedCoupon(Member member, Coupon coupon) {
        if (userCouponRepository.existsByMemberAndCoupon(member, coupon)) {
                throw new InvalidCouponException("이미 발급받은 쿠폰 입니다");
        }
    }

    private void validateCouponExpiration(LocalDate expirationDate) {
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new InvalidCouponException("쿠폰이 이미 만료되었습니다");
        }
    }

    private Coupon findCouponByCouponId(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundCouponException("존재 하지 않는 쿠폰 입니다."));
    }

    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("존재 하지 않은 사용자 입니다"));
    }
}
