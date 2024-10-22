package forOlderJava.absurdityAppForJava.domain.cart.repository;

import forOlderJava.absurdityAppForJava.domain.cart.CartItem;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteByMember(@Param("member")Member member);
}
