package forOlderJava.absurdityAppForJava.domain.member.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S")
public class GradeScheduler {
}
