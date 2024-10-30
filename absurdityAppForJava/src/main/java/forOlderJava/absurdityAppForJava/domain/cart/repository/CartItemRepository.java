package forOlderJava.absurdityAppForJava.domain.cart.repository;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.cart.CartItem;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci where ci.id = :id ORDER BY ci.createdAt")
    List<CartItem> findAllByCartItemIdOrderByCreatedAt(Long id);

    List<CartItem> findAllByCartOrderByCreatedAt(Cart cart);

    @Modifying
    @Query("delete from CartItem ci"
    + " where ci.cart ="
    + " (select c from Cart c where c.member = :member)")
    void deleteByMember(@Param("member")Member member);
}
