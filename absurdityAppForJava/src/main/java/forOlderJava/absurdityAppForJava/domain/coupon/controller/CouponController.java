package forOlderJava.absurdityAppForJava.domain.coupon.controller;

import forOlderJava.absurdityAppForJava.domain.coupon.exception.CouponException;
import forOlderJava.absurdityAppForJava.domain.coupon.service.CouponService;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterCouponCommand;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterCouponRequest;
import forOlderJava.absurdityAppForJava.domain.coupon.service.request.RegisterUserCouponCommand;
import forOlderJava.absurdityAppForJava.domain.coupon.service.response.FindCouponsResponse;
import forOlderJava.absurdityAppForJava.domain.coupon.service.response.FindIssuedCouponsResponse;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody RegisterCouponRequest couponRequest) {
        RegisterCouponCommand registerCouponCommand = RegisterCouponCommand.from(couponRequest);
        Long couponId = couponService.createCoupon(registerCouponCommand);
        URI location = URI.create("/v1/coupons/" + couponId);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/my-coupons/{couponId}")
    public ResponseEntity<Void> RegisterUserCoupon(@PathVariable(value = "couponId") final Long couponId,
                                                   @LoginUser final Long memberId) {

        RegisterUserCouponCommand userCouponCommand = RegisterUserCouponCommand.of(memberId, couponId);
        Long userCouponId = couponService.registerUserCoupon(userCouponCommand);

        URI location = URI.create("/v1/coupons/" + userCouponId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<FindCouponsResponse> findCoupons() {
        FindCouponsResponse findCouponsResponse = couponService.findCoupons();
        return ResponseEntity.ok(findCouponsResponse);
    }

    @GetMapping("/my-coupons")
    public ResponseEntity<FindIssuedCouponsResponse> findIssuedCoupons(@LoginUser final Long memberId) {
        FindIssuedCouponsResponse couponsResponse = couponService.findIssuedCoupons(memberId);
        return ResponseEntity.ok(couponsResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTemplate> handleException(final CouponException couponException) {
        log.info(couponException.getMessage());
        return ResponseEntity.badRequest().body(ErrorTemplate.of(couponException.getMessage()));
    }

}
