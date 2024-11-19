package forOlderJava.absurdityAppForJava.domain.event.controller;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.exception.NotFoundEventException;
import forOlderJava.absurdityAppForJava.domain.event.repository.EventRepository;
import forOlderJava.absurdityAppForJava.domain.event.service.EventCacheService;
import forOlderJava.absurdityAppForJava.domain.event.service.EventItemService;
import forOlderJava.absurdityAppForJava.domain.event.service.EventRedisDto;
import forOlderJava.absurdityAppForJava.domain.event.service.EventService;
import forOlderJava.absurdityAppForJava.domain.event.service.request.*;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventsResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse.*;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {

    private static final String BASE_URL = "/v1/events/";
    private final EventService eventService;
    private final EventItemService eventItemService;
    private final EventCacheService eventCacheService;
    private final EventRepository eventRepository;
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Void> registerEvent(@RequestBody @Valid RegisterEventRequest request) {
        RegisterEventCommand registerEventCommand = RegisterEventCommand.of(request.title(), request.description());
        Long eventId = eventService.registerEvent(registerEventCommand);
        URI location = URI.create(BASE_URL + eventId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<FindEventsResponse> findEvents() {
        List<EventRedisDto> recentEvents = eventCacheService.getRecentEvents(100);

        if (!recentEvents.isEmpty()) {
            return ResponseEntity.ok(FindEventsResponse.fromRedis(recentEvents));
        }

        FindEventsResponse response = eventService.findEvents();

        response.events().forEach(event -> eventCacheService.cacheEventInfo(EventRedisDto.from(event, LocalDateTime.now())));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FindEventDetailResponse> findEvent(@PathVariable(value = "eventId") final Long eventId) {
        return eventCacheService.getEventInfo(eventId)
                .map(eventRedisDto -> {
                    // 아이템 정보도 캐시에서 조회
                    List<Long> itemIds = eventCacheService.getEventItemIds(eventId);

                    //EventDetailResponse 조회
                    EventDetailResponse eventDetail = new EventDetailResponse(
                            eventRedisDto.eventId(),
                            eventRedisDto.title(),
                            eventRedisDto.description());

                    List<EventItemResponse> items = itemService.findItemsByIds(
                                    eventCacheService.getEventItemIds(eventId)
                            ).stream()
                            .map(item -> new EventItemResponse(
                                    item.getId(),
                                    item.getName(),
                                    item.getPrice(),
                                    item.getDiscount(),
                                    item.getRate()
                            )).collect(Collectors.toList());

                    return ResponseEntity.ok(FindEventDetailResponse.of(eventDetail, items));
                })
                .orElseGet(() -> {
                    // DB에서 이벤트 상세 조회 (fetch join 사용)
                    Event event = eventRepository.findByIdWithEventItems(eventId)
                            .orElseThrow(() -> new NotFoundEventException("Event not found"));

                    // 응답 생성
                    EventDetailResponse eventDetail = new EventDetailResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getDescription()
                    );

                    // 아이템 정보 변환
                    List<EventItemResponse> items = event.getEventItems().stream()
                            .map(eventItem -> new EventItemResponse(
                                    eventItem.getId(),
                                    eventItem.getItem().getName(),
                                    eventItem.getItem().getPrice(),
                                    eventItem.getItem().getDiscount(),
                                    eventItem.getItem().getRate()
                            )).collect(Collectors.toList());

                    // 캐시 저장
                    eventCacheService.cacheEventInfo(EventRedisDto.from(event));
                    eventCacheService.cacheEventItems(eventId,
                            items.stream()
                                    .map(EventItemResponse::itemId)
                                    .collect(Collectors.toList())
                    );

                    return ResponseEntity.ok(FindEventDetailResponse.of(eventDetail, items));
                });
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerEventItems(@RequestBody @Valid RegisterEventItemsRequest request,
                                                   @PathVariable(value = "eventId") final Long eventId) {
        RegisterEventItemsCommand registerEventItemsCommand = RegisterEventItemsCommand.of(eventId, request.items());
        Long saved = eventItemService.registerEventItems(registerEventItemsCommand);
        URI location = URI.create(BASE_URL + saved);
        return ResponseEntity.created(location).build();
    }


}
