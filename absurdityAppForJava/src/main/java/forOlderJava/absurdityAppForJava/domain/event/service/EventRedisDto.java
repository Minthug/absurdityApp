package forOlderJava.absurdityAppForJava.domain.event.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.EventItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record EventRedisDto(Long eventId, String title, String description,
                            @JsonSerialize(using = LocalDateTimeSerializer.class)
                            @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                            LocalDateTime createdAt,
                            List<Long> eventItemIds) {

    public static EventRedisDto from(final Event event) {
        return new EventRedisDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCreatedAt(),
                event.getEventItems().stream()
                        .map(EventItem::getId)
                        .collect(Collectors.toList())
        );
    }
}
