package forOlderJava.absurdityAppForJava.domain.event.service;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.EventItem;
import forOlderJava.absurdityAppForJava.domain.event.repository.EventRepository;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final EventRepository eventRepository;

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

    /**
     * 캐시 미스 전략 추가
     * @param eventId
     * @return
     */
    public Optional<EventRedisDto> getEventInfo(Long eventId) {

        String key = EVENT_INFO_PREFIX + eventId;
        EventRedisDto cacheEvent = (EventRedisDto) redisTemplate.opsForValue().get(key);

        if (cacheEvent != null) {
            return Optional.of(cacheEvent);
        }

        return eventRepository.findById(eventId)
                .map(event -> {
                    EventRedisDto eventRedisDto = EventRedisDto.from(event);
                    cacheEventInfo(eventRedisDto);
                    return eventRedisDto;
                });
    }


    /**
     * 캐시 미스 전략 추가
     * @param limit
     * @return
     */
    public List<EventRedisDto> getRecentEvents(int limit) {
        Set<Object> events = redisTemplate.opsForZSet()
                .reverseRange(RECENT_EVENTS_KEY, 0, limit -1);

        if (events != null || !events.isEmpty()) {
            return events.stream()
                    .filter(obj -> obj instanceof EventRedisDto)
                    .map(obj -> (EventRedisDto) obj)
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        List<Event> recentEvents = eventRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<EventRedisDto> eventRedisDtos = recentEvents.stream()
                .map(EventRedisDto::from)
                .collect(Collectors.toList());

        eventRedisDtos.forEach(dto -> {
            cacheEventInfo(dto);
            addToRecentEvents(dto);
        });

        return eventRedisDtos;
    }

    public List<Long> getEventItemIds(Long eventId) {
        String key = EVENT_ITEMS_PREFIX + eventId;
        List<Object> itemIds = redisTemplate.opsForList().range(key, 0, -1);

        if (itemIds != null && !itemIds.isEmpty()) {
            return itemIds.stream()
                    .map(obj -> Long.parseLong(String.valueOf(obj)))
                    .collect(Collectors.toList());
        }

        return eventRepository.findByIdWithEventItems(eventId)
                .map(event -> event.getEventItems().stream()
                        .map(EventItem::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
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
