package forOlderJava.absurdityAppForJava.domain.notification.service;

import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.Notification;
import forOlderJava.absurdityAppForJava.domain.notification.NotificationType;
import forOlderJava.absurdityAppForJava.domain.notification.repository.EmitterRepository;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.ConnectNotificationCommand;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.SendNotificationCommand;
import forOlderJava.absurdityAppForJava.domain.notification.service.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

import static java.text.MessageFormat.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 120;

    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;

    public SseEmitter connectNotification(ConnectNotificationCommand connectNotificationCommand) {
        Long memberId = connectNotificationCommand.memberId();
        String lastEventId = connectNotificationCommand.lastEventId();

        String emitterId = format("{0}_{1}", memberId, System.currentTimeMillis());
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, emitter);

        // 연결 종료/에러시 emitter 제거
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError(e -> emitterRepository.deleteById(emitterId));


        send(emitter, emitterId, format("[connected] MemberId={0}", memberId));

        if (!connectNotificationCommand.lastEventId().isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllByIdStartWith(memberId);
            events.entrySet().stream().filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> send(emitter, emitterId, entry.getValue()));
        }

        return emitter;
    }

    public void sendNotification(SendNotificationCommand sendNotificationCommand) {
        Long memberId = sendNotificationCommand.memberId();
        String title = sendNotificationCommand.title();
        String content = sendNotificationCommand.content();
        NotificationType notificationType = sendNotificationCommand.notificationType();

        verifyExistsUser(memberId);
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .memberId(memberId)
                .notificationType(notificationType)
                .build();

        Map<String, SseEmitter> emitters = emitterRepository.findAllByIdStartWith(memberId);
        emitters.forEach((key, emitter) -> {
            send(emitter, key, NotificationResponse.from(notification));
        });
    }

    private void verifyExistsUser(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않은 유저 입니다"));
    }

    /**
     * 알림 객체 전송용
     * @param emitter
     * @param emitterId
     * @param data
     */
    private void send(SseEmitter emitter, String emitterId, NotificationResponse data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name(data.notificationType().getValue())
                    .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(emitterId);
        }
    }

    /**
     *
     * 일반 데이터 전송용
     * @param emitter
     * @param emitterId
     * @param data
     */
    private void send(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.error("알림 전송에 실패했습니다", e);
        }
    }
}

