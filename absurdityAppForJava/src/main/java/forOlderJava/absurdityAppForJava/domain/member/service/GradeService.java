package forOlderJava.absurdityAppForJava.domain.member.service;

import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberOrderCount;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService {

    private static final int ONE = 1;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMemberGrade() {
        LocalDateTime startTimeOfPreviousMonth = getStartTimeOfPreviousMonth();
        LocalDateTime lastTImeOfPreviousMonth = getEndTimeOfPreviousMonth();

        List<MemberOrderCount> memberOrderCounts = memberRepository.getMemberOrderCount(
                startTimeOfPreviousMonth,
                lastTImeOfPreviousMonth);

        Map<MemberGrade, List<MemberOrderCount>> memberGradeGroup = groupByMemberGrader(memberOrderCounts);
        memberGradeGroup.forEach(((memberGrade, memberOrderCountGroup) ->
                memberRepository.updateMemberGrade(memberGrade, extractMemberIds(memberOrderCountGroup))));

    }

    private List<Long> extractMemberIds(List<MemberOrderCount> memberOrderGradeGroup) {
        return memberOrderGradeGroup.stream()
                .map(MemberOrderCount::getMemberId)
                .toList();
    }

    private Map<MemberGrade, List<MemberOrderCount>> groupByMemberGrader(List<MemberOrderCount> memberOrderCounts) {
        return memberOrderCounts.stream()
                .collect(groupingBy(memberOrderCount ->
                        MemberGrade.calculateMemberGrade(memberOrderCount.getOrderCount())));
    }

    private LocalDateTime getEndTimeOfPreviousMonth() {
        return YearMonth.now()
                .atDay(ONE)
                .atStartOfDay()
                .minusMonths(ONE);
    }

    private LocalDateTime getStartTimeOfPreviousMonth() {
        return YearMonth.now()
                .minusMonths(ONE)
                .atDay(ONE)
                .atStartOfDay();
    }
}
