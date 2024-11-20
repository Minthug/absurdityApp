package forOlderJava.absurdityAppForJava.domain.coupon.service.response;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;

import java.time.LocalDate;


public record CouponRedisDto(Long couponId, Integer discount, String name, String description, LocalDate endAt, Integer minOrderPrice) {

    public static CouponRedisDto from(final Coupon coupon) {
        return new CouponRedisDto(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getName(),
                coupon.getDescription(),
                coupon.getEndAt(),
                coupon.getMinOrderPrice()
        );
    }


}
