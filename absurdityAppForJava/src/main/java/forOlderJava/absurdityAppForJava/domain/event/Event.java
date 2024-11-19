package forOlderJava.absurdityAppForJava.domain.event;

import forOlderJava.absurdityAppForJava.domain.event.exception.InvalidEventDescriptionException;
import forOlderJava.absurdityAppForJava.domain.event.exception.InvalidEventTitleException;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<EventItem> eventItems = new ArrayList<>();

    public Event(String title, String description) {
        validateTitle(title);
        validateDescription(description);
        this.title = title;
        this.description = description;
    }

    private void validateDescription(String description) {
        if (isNull(description)) throw new InvalidEventDescriptionException("이벤트 설명이 존재하지 않습니다");
    }

    private void validateTitle(String title) {
        if (isNull(title)) throw new InvalidEventTitleException("이벤트 제목이 존재하지 않습니다");
    }


}
