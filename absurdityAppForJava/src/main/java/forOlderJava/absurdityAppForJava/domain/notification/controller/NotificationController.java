package forOlderJava.absurdityAppForJava.domain.notification.controller;

import forOlderJava.absurdityAppForJava.domain.notification.service.NotificationService;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.ConnectNotificationCommand;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> sseConnection(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                                    @LoginUser Long memberId) {

        ConnectNotificationCommand connectNotificationCommand = ConnectNotificationCommand.of(memberId, lastEventId);
        SseEmitter sseEmitter = notificationService.connectNotification(connectNotificationCommand);
        return ResponseEntity.ok(sseEmitter);
    }
}
