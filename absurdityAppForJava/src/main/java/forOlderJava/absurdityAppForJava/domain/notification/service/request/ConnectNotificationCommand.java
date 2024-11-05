package forOlderJava.absurdityAppForJava.domain.notification.service.request;

public record ConnectNotificationCommand(Long memberId, String lastEventId) {

    public static ConnectNotificationCommand of(final Long memberId, final String lastEventId) {
        return new ConnectNotificationCommand(memberId, lastEventId);
    }
}
