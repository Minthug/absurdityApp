package forOlderJava.absurdityAppForJava.domain.event.controller;

import forOlderJava.absurdityAppForJava.domain.event.service.EventItemService;
import forOlderJava.absurdityAppForJava.domain.event.service.EventService;
import forOlderJava.absurdityAppForJava.domain.event.service.request.*;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {

    private static final String BASE_URL = "/v1/events/";
    private final EventService eventService;
    private final EventItemService eventItemService;

    @PostMapping
    public ResponseEntity<Void> registerEvent(@RequestBody @Valid RegisterEventRequest request) {
        RegisterEventCommand registerEventCommand = RegisterEventCommand.of(request.title(), request.description());
        Long eventId = eventService.registerEvent(registerEventCommand);
        URI location = URI.create(BASE_URL + eventId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<FindEventsResponse> findEvents() {
        return ResponseEntity.ok(eventService.findEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FindEventDetailResponse> findEvent(@PathVariable(value = "eventId") final Long eventId) {
        FindEventDetailCommand findEventDetailCommand = FindEventDetailCommand.from(eventId);
        return ResponseEntity.ok(eventService.findEventDetail(findEventDetailCommand));
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerEventItems(@RequestBody @Valid RegisterEventItemsRequest request,
                                                   @PathVariable(value = "eventId") final Long eventId) {
        RegisterEventItemsCommand registerEventItemsCommand = RegisterEventItemsCommand.of(eventId, request.items());
        Long saved = eventItemService.registerEventItems(registerEventItemsCommand);
        URI location = URI.create(BASE_URL + saved);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
}
