package forOlderJava.absurdityAppForJava.domain.member.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    List<MemberOrderCount> getMemberOrderCount(@Param("start")LocalDateTime start, @Param("end") LocalDateTime end);

    void updateMemberGrade(@Param("memberGrade")MemberGrade memberGrade,
                           @Param("MemberIds") List<Long> memberIds);

}
