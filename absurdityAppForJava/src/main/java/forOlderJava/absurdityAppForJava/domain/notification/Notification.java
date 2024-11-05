package forOlderJava.absurdityAppForJava.domain.notification;

import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    private static final int TITLE_LENGTH = 30;
    private static final int CONTENT_LENGTH = 100;

    private String title;
    private String content;
    private NotificationType notificationType;
    private Long memberId;

    @Builder
    public Notification(String title, String content, NotificationType notificationType, Long memberId) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.notificationType = notificationType;
        this.memberId = memberId;
    }

    private void validateContent(String content) {
        if (Objects.nonNull(content) && content.length() > CONTENT_LENGTH) {
            throw new InvalidNotificationException("내용의 길이는 50자 이하 이여야 합니다");
        }
    }

    private void validateTitle(String title) {
        if (Objects.nonNull(title) && title.length() > TITLE_LENGTH) {
            throw new InvalidNotificationException("제목의 길이는 20자 이하 이여야 합니다.");
        }
    }


}
