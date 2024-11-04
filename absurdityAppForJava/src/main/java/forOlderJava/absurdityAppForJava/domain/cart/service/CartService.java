package forOlderJava.absurdityAppForJava.domain.cart.service;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.cart.CartItem;
import forOlderJava.absurdityAppForJava.domain.cart.exception.NotFoundCartException;
import forOlderJava.absurdityAppForJava.domain.cart.exception.NotFoundCartItemException;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartItemRepository;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartRepository;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.RegisterCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.UpdateCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.cart.service.response.FindCartItemResponse;
import forOlderJava.absurdityAppForJava.domain.cart.service.response.FindCartItemsResponse;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void deleteCartItem(final Long cartItemId) {
        CartItem foundCartItem = findCartItemByCartItemId(cartItemId);
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public FindCartItemsResponse findCartItemsByMemberId(final Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("해당 사용자를 찾을 수 없습니다"));

        Cart foundCart = cartRepository.findByMember(foundMember)
                .orElseThrow(() -> new NotFoundCartException("해당 장바구니를 찾을 수 없습니다"));

        List<CartItem> foundCartItems = cartItemRepository.findAllByCartOrderByCreatedAt(foundCart);

        return FindCartItemsResponse.from(foundCartItems
                .stream()
                .map(cartItem -> FindCartItemResponse.of(
                        cartItem.getCart().getId(),
                        cartItem.getItem().getId(),
                        cartItem.getQuantity()
                )).toList());
    }

    @Transactional
    public int getCartItemTotalPrice(final long cartItemId) {
        int totalPrice = 0;

        List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdOrderByCreatedAt(cartItemId);

        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();
        }

        return totalPrice;
    }

    @Transactional
    public void updateCartItemQuantity(UpdateCartItemCommand command) {
        CartItem foundCartItem = findCartItemByCartItemId(command.cartId());

        foundCartItem.changeQuantity(command.quantity());
    }

    private CartItem findCartItemByCartItemId(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundCartItemException("장바구니 상품이 존재하지 않습니다"));
    }
}
