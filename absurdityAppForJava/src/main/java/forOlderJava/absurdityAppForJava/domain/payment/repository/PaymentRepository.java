package forOlderJava.absurdityAppForJava.domain.payment.repository;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_UuidAndMember_MemberId(String uuid, Long memberId);

    void deleteByMember(Member member);
}
