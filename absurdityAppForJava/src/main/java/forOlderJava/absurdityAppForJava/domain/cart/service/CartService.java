package forOlderJava.absurdityAppForJava.domain.cart.service;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.cart.CartItem;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartItemRepository;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartRepository;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.RegisterCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long registerCartItem(RegisterCartItemCommand command) {
        Member foundMember = memberRepository.findById(command.memberId())
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않은 사용자 입니다"));

        Cart foundCart = cartRepository.findByMember(foundMember)
                .orElseGet(() -> {
                    Cart savedCart = cartRepository.save(new Cart(foundMember));
                    return savedCart;
                });

        Item foundItem = itemRepository.findByItemId(command.itemId())
                .orElseThrow(() -> new NotFoundItemException("존재하지 않은 상품 입니다"));

        CartItem cartItem = CartItem.builder()
                .cart(foundCart)
                .item(foundItem)
                .quantity(command.quantity())
                .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return savedCartItem.getId();  // getId() == getCartItemId();
    }

}
