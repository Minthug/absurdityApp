package forOlderJava.absurdityAppForJava.domain.member.scheduler;

import forOlderJava.absurdityAppForJava.domain.member.service.GradeService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S")
public class GradeScheduler {

    private final GradeService gradeService;

    @Async
    @Scheduled(cron = "0 0 5 1 * *")
    @SchedulerLock(name = "Update_Member_Grade", lockAtLeastFor = "PT10S")
    public void updateMemberGradeScheduler() {
        gradeService.updateMemberGrade();
    }

}
