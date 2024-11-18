package forOlderJava.absurdityAppForJava.domain.member.service;

import forOlderJava.absurdityAppForJava.domain.member.service.response.MemberRedisDto;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.YoungerRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class MemberCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ACTIVE_MEMBERS_KEY = "active_members";
    private static final String POPULAR_YOUNGER_KEY = "popular_younger";

    public void saveActiveMember(MemberRedisDto memberRedisDto) {
        redisTemplate.opsForZSet().add(
                ACTIVE_MEMBERS_KEY,
                memberRedisDto,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        );
    }

    public void savePopularYounger(YoungerRedisDto youngerRedisDto) {
        redisTemplate.opsForZSet().add(
                POPULAR_YOUNGER_KEY,
                youngerRedisDto,
                youngerRedisDto.rating()
        );
    }
}
