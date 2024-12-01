package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.repository.EmitterRepository;
import forOlderJava.absurdityAppForJava.domain.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    }
}
