package forOlderJava.absurdityAppForJava.domain.cart.repository;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember(final Member member);

    void deleteByMember(Member member);
}
