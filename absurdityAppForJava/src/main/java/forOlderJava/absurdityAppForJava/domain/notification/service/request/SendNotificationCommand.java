package forOlderJava.absurdityAppForJava.domain.notification.service.request;

import forOlderJava.absurdityAppForJava.domain.notification.NotificationType;

public record SendNotificationCommand(Long memberId, String title, String content, NotificationType notificationType) {

    public static SendNotificationCommand of(final Long memberId, final String title, final String content,
                                             final NotificationType notificationType) {
        return new SendNotificationCommand(memberId, title, content, notificationType);
    }
}
