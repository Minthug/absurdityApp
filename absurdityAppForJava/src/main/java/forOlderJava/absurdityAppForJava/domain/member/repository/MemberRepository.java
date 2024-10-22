package forOlderJava.absurdityAppForJava.domain.member.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

}
