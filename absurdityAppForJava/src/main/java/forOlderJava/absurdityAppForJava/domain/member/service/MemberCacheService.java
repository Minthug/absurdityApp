package forOlderJava.absurdityAppForJava.domain.member.service;

import forOlderJava.absurdityAppForJava.domain.member.service.response.MemberRedisDto;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.YoungerRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MEMBER_INFO_PREFIX = "member:info";
    private static final String ACTIVE_MEMBERS_KEY = "active_members";
    private static final String POPULAR_YOUNGER_KEY = "popular_younger";

    public void cacheMemberInfo(MemberRedisDto memberRedisDto) {
        redisTemplate.opsForValue().set(
            MEMBER_INFO_PREFIX + memberRedisDto.memberId(),
                memberRedisDto,
                Duration.ofHours(24)
        );
    }

    // 멤버 정보 조회
    public Optional<MemberRedisDto> getMemberInfo(Long memberId) {
        return Optional.ofNullable(
                (MemberRedisDto) redisTemplate.opsForValue().get(MEMBER_INFO_PREFIX + memberId)
        );
    }

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

    public void deleteMemberCache(Long memberId) {
        redisTemplate.delete(MEMBER_INFO_PREFIX + memberId);
    }
}
