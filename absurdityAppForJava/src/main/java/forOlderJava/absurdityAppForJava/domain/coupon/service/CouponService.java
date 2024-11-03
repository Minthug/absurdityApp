package forOlderJava.absurdityAppForJava.domain.coupon.service;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.CouponRepository;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.UserCouponRepository;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterCouponCommand;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
