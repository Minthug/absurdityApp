package forOlderJava.absurdityAppForJava.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_COUPON_PREFIX = "user:coupon";
    private static final String USER_AVAILABLE_COUPONS = "user:%d:available-coupons";

    public void cacheUserAvailableCoupons(Long memberId, List<Long> couponIds) {
        String key = String.format(USER_AVAILABLE_COUPONS, memberId);
        redisTemplate.opsForList().rightPushAll(key, couponIds.stream()
                .map(String::valueOf)
                .toArray(String[]::new));
        redisTemplate.expire(key, Duration.ofHours(24));
    }

    public void markCouponAsUsed(Long memberId, Long couponId) {
        String key = String.format(USER_AVAILABLE_COUPONS, memberId);
        redisTemplate.opsForList().remove(key, 0, couponId.toString());
    }
}
