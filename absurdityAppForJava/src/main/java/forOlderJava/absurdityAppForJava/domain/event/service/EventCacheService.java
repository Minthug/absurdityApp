package forOlderJava.absurdityAppForJava.domain.event.service;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EVENT_INFO_PREFIX = "event:info:";
    private static final String RECENT_EVENTS_KEY = "events:recent";
    private static final String EVENT_ITEMS_PREFIX = "event:items";

    public void cacheEventInfo(EventRedisDto eventRedisDto) {
        redisTemplate.opsForValue().set(
                EVENT_INFO_PREFIX + eventRedisDto.eventId(),
                eventRedisDto,
                Duration.ofHours(24)
        );
    }

    public void addToRecentEvents(EventRedisDto eventRedisDto) {
        redisTemplate.opsForZSet().add(
                RECENT_EVENTS_KEY,
                eventRedisDto,
                eventRedisDto.createdAt().toEpochSecond(ZoneOffset.UTC)
        );

        redisTemplate.opsForZSet().removeRange(RECENT_EVENTS_KEY, 0, -101);
    }

    public void cacheEventItems(Long eventId, List<Long> itemIds) {
        String key = EVENT_ITEMS_PREFIX + eventId;
        redisTemplate.opsForList().rightPushAll(
                key,
                itemIds.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new));
        redisTemplate.expire(key, Duration.ofHours(24));
    }

    public Optional<EventRedisDto> getEventInfo(Long eventId) {
        return Optional.ofNullable(
                (EventRedisDto) redisTemplate.opsForValue()
                        .get(EVENT_INFO_PREFIX + eventId)
        );
    }

    public List<EventRedisDto> getRecentEvents(int limit) {
        Set<Object> events = redisTemplate.opsForZSet()
                .reverseRange(RECENT_EVENTS_KEY, 0, limit -1);

        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .filter(obj -> obj instanceof EventRedisDto)
                .map(obj -> (EventRedisDto) obj)
                .collect(Collectors.toList());
    }

    public List<Long> getEventItemIds(Long eventId) {
        String key = EVENT_ITEMS_PREFIX + eventId;
        List<Object> itemIds = redisTemplate.opsForList()
                .range(key, 0, -1);

        if (itemIds == null || itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        return itemIds.stream()
                .map(obj -> Long.parseLong(String.valueOf(obj)))
                .collect(Collectors.toList());
    }


    public void deleteEventCache(Long eventId) {
        redisTemplate.delete(EVENT_INFO_PREFIX + eventId);
        redisTemplate.delete(EVENT_ITEMS_PREFIX + eventId);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void cleanOldEvent() {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        redisTemplate.opsForZSet().removeRangeByScore(
                RECENT_EVENTS_KEY,
                0,
                weekAgo.toEpochSecond(ZoneOffset.UTC)
        );
    }

}
