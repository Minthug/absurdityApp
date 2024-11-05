package forOlderJava.absurdityAppForJava.domain.notification.service;

import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.repository.EmitterRepository;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.ConnectNotificationCommand;
import forOlderJava.absurdityAppForJava.domain.notification.service.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

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

    private void send(SseEmitter emitter, String emitterId, NotificationResponse response) {

    }

    private void send(SseEmitter emitter, String emitterId, Object data) {

    }
}

