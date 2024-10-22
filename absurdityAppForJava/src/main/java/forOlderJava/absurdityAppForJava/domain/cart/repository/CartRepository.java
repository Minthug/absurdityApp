package forOlderJava.absurdityAppForJava.domain.cart.repository;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    void deleteByMember(Member member);
}
