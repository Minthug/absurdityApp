package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.NotificationType;
import forOlderJava.absurdityAppForJava.domain.notification.repository.EmitterRepository;
import forOlderJava.absurdityAppForJava.domain.notification.service.NotificationService;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.ConnectNotificationCommand;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.SendNotificationCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private EmitterRepository emitterRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("알림 연결 테스트")
    void connectNotificationTest() {
        Long memberId = 1L;
        String lastEventId = "";
        String emitterId = memberId + "_" + System.currentTimeMillis();

        ConnectNotificationCommand command = new ConnectNotificationCommand(memberId, lastEventId);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(createTestMember(memberId)));

        SseEmitter emitter = notificationService.connectNotification(command);

        assertThat(emitter).isNotNull();
        verify(emitterRepository).save(anyString(), any(SseEmitter.class));
    }

    @Test
    @DisplayName("알림 전송 테스트")
    void sendNotificationTest() throws IOException {

        Long memberId = 1L;
        String title = "테스트 알림";
        String connect = "테스트 내용";
        NotificationType notificationType = NotificationType.CONNECT;


        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put("test_1", new SseEmitter());

        SendNotificationCommand command = SendNotificationCommand.builder()
                    .memberId(memberId)
                    .title("테스트 알림")
                    .content("테스트 내용")
                    .notificationType(NotificationType.CONNECT)
                    .build();

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(createTestMember(memberId)));

        when(emitterRepository.findAllByIdStartWith(memberId))
                .thenReturn(emitters);

        notificationService.sendNotification(command);

        System.out.println(verify(memberRepository).findById(memberId));
        verify(emitterRepository).findAllByIdStartWith(memberId);
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("test@email.com")
                .nickname("tester")
                .build();
    }

    void sendAndReceiveNotificationTest() {

    }
}
