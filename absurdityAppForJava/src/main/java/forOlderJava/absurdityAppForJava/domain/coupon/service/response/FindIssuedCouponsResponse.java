package forOlderJava.absurdityAppForJava.domain.coupon.service.response;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record FindIssuedCouponsResponse(List<FindIssuedCouponResponse> coupons) {

    public static FindIssuedCouponsResponse from(final List<UserCoupon> issuedCoupons) {
        return issuedCoupons.stream()
                .map(UserCoupon::getCoupon)
                .map(FindIssuedCouponResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), FindIssuedCouponsResponse::new));

    }


    public record FindIssuedCouponResponse(Long couponId, String name, String description, Integer discount, LocalDate endAt) {

        public static FindIssuedCouponResponse from(final Coupon coupon) {
            return new FindIssuedCouponResponse(
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getDescription(),
                    coupon.getDiscount(),
                    coupon.getEndAt()
            );
        }
    }
}
