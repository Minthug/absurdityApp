package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.repository.EmitterRepository;
import forOlderJava.absurdityAppForJava.domain.notification.service.NotificationService;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.ConnectNotificationCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class NotificationServiceTest {
    @MockBean
    private EmitterRepository emitterRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
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
        verify(emitterRepository).findAllByIdStartWith(memberId);
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("test@email.com")
                .nickname("tester")
                .build();
    }
}
