package forOlderJava.absurdityAppForJava.domain.coupon.service;

import forOlderJava.absurdityAppForJava.domain.coupon.service.response.CouponRedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String COUPON_INFO_PREFIX = "coupon:info";
    private static final String AVAILABLE_COUPON_KEY = "coupons:available";

    public void cacheCouponInfo(CouponRedisDto couponRedisDto) {
        redisTemplate.opsForValue().set(
                COUPON_INFO_PREFIX + couponRedisDto.couponId(),
                couponRedisDto,
                Duration.ofDays(1)
        );
    }

    public void addToAvailableCoupons(CouponRedisDto couponRedisDto) {
        redisTemplate.opsForZSet().add(
                AVAILABLE_COUPON_KEY,
                couponRedisDto,
                couponRedisDto.endAt().toEpochDay()
        );
    }

    public Optional<CouponRedisDto> getCouponInfo(Long couponId) {
        return Optional.ofNullable(
                (CouponRedisDto) redisTemplate.opsForValue().get(COUPON_INFO_PREFIX + couponId)
        );
    }

    public List<CouponRedisDto> getAvailableCoupons() {
        long today = LocalDate.now().toEpochDay();
        Set<Object> coupons = redisTemplate.opsForZSet()
                .rangeByScore(AVAILABLE_COUPON_KEY, today, Double.POSITIVE_INFINITY);

        if (coupons == null) {
            return Collections.emptyList();
        }

        return coupons.stream()
                .map(obj -> (CouponRedisDto) obj)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredCoupons() {
        long today = LocalDate.now().toEpochDay();
        redisTemplate.opsForZSet().removeRangeByScore(
                AVAILABLE_COUPON_KEY,
                Double.NEGATIVE_INFINITY,
                today - 1
        );
    }
}
