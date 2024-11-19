package forOlderJava.absurdityAppForJava.domain.event.service.response;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.service.EventRedisDto;

import java.util.List;
import java.util.stream.Collectors;

public record FindEventsResponse(List<FindEventResponse> events) {

    public static FindEventsResponse of(final List<FindEventResponse> events) {
        return new FindEventsResponse(events);
    }

    public static FindEventsResponse fromRedis(List<EventRedisDto> events) {
        return new FindEventsResponse(
                events.stream()
                        .map(FindEventResponse::from)
                        .collect(Collectors.toList()));
    }

    public record FindEventResponse(Long eventId, String title, String description) {

        public static FindEventResponse from(Event event) {
            return new FindEventResponse(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription());
        }

        public static FindEventResponse from(EventRedisDto eventRedisDto) {
            return new FindEventResponse(
                    eventRedisDto.eventId(),
                    eventRedisDto.title(),
                    eventRedisDto.description());
        }
    }
}
