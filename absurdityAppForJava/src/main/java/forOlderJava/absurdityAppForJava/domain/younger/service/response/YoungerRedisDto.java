package forOlderJava.absurdityAppForJava.domain.younger.service.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import forOlderJava.absurdityAppForJava.domain.younger.Younger;

import java.time.LocalDateTime;

public record YoungerRedisDto(Long youngerId, String username, String location, Double rating, Integer ratingCount,
                              @JsonSerialize(using = LocalDateTimeSerializer.class)
                              @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                              LocalDateTime lastActiveAt) {

    public static YoungerRedisDto from(final Younger younger) {
        return new YoungerRedisDto(younger.getId(), younger.getUsername(),
                younger.getLocation(), younger.getRating(), younger.getRatingCount(),
                younger.getUpdatedAt());
    }
}
