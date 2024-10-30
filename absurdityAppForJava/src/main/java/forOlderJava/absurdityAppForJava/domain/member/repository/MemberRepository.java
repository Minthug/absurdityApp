package forOlderJava.absurdityAppForJava.domain.member.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    @Query("select o.member.id as memberId, count(o) as orderCount from Order o"
    + " where o.createdAt between :start and :end"
    + " group by o.member.id")
    List<MemberOrderCount> getMemberOrderCount(@Param("start")LocalDateTime start, @Param("end") LocalDateTime end);


    @Modifying
    @Query("update Member m set m.memberGrade = :memberGrade"
    + " where m.id in :memberIds")
    void updateMemberGrade(@Param("memberGrade")MemberGrade memberGrade,
                           @Param("memberIds") List<Long> memberIds);

}
