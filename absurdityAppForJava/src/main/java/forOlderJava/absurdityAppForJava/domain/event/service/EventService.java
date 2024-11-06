package forOlderJava.absurdityAppForJava.domain.event.service;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.exception.NotFoundEventException;
import forOlderJava.absurdityAppForJava.domain.event.repository.EventRepository;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse.EventDetailResponse;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventsResponse;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventsResponse.FindEventResponse;
import forOlderJava.absurdityAppForJava.domain.event.service.request.FindEventDetailCommand;
import forOlderJava.absurdityAppForJava.domain.event.service.request.RegisterEventCommand;
import forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static forOlderJava.absurdityAppForJava.domain.event.service.response.FindEventDetailResponse.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long registerEvent(RegisterEventCommand registerEventCommand) {
        Event event = new Event(registerEventCommand.title(), registerEventCommand.description());
        Event registered = eventRepository.save(event);

        return registered.getId();
    }

    @Transactional(readOnly = true)
    public FindEventsResponse findEvents() {
        List<Event> events = eventRepository.findAllByOrderByCreatedAtDesc();
        return FindEventsResponse.of(events.stream()
                .map(event -> new FindEventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription()
                )).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public FindEventDetailResponse findEventDetail(FindEventDetailCommand findEventDetailCommand) {
        Event event = eventRepository.findByIdWithEventItems(findEventDetailCommand.eventId())
                .orElseThrow(() -> new NotFoundEventException("존재하지 않은 이벤트 입니다"));

        EventDetailResponse eventDetailResponse = new EventDetailResponse(event.getId(), event.getTitle(), event.getDescription());

        List<EventItemResponse> eventItemResponses = event.getEventItems().stream()
                .map(eventItem -> new EventItemResponse(
                        eventItem.getItem().getId(),
                        eventItem.getItem().getName(),
                        eventItem.getItem().getPrice(),
                        eventItem.getItem().getDiscount(),
                        eventItem.getItem().getRate())
                ).collect(Collectors.toList());

        return FindEventDetailResponse.of(eventDetailResponse, eventItemResponses);
    }
}
