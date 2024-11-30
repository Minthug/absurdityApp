package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.cart.Cart;
import forOlderJava.absurdityAppForJava.domain.cart.CartItem;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartItemRepository;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartRepository;
import forOlderJava.absurdityAppForJava.domain.cart.service.CartService;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.RegisterCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.cart.service.response.FindCartItemsResponse;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CartServiceTest {

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("장바구니 아이템 등록 테스트")
    void registerCartItemTest() {
        Long memberId = 1L;
        Long itemId = 1L;
        int quantity = 2;

        Member member = createTestMember(memberId);
        Item item = createTestItem(itemId);
        Cart cart = createTestCart(member);

        CartItem cartItem = createTestCartItem(cart, item, quantity);
        RegisterCartItemCommand cartItemCommand = new RegisterCartItemCommand(memberId, itemId, quantity);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(cartRepository.findByMember(member))
                .willReturn(Optional.of(cart));
        given(itemRepository.findByItemId(itemId))
                .willReturn(Optional.of(item));
        given(cartItemRepository.save(any(CartItem.class)))
                .willReturn(cartItem);

        Long savedCartItemId = cartService.registerCartItem(cartItemCommand);

        assertThat(savedCartItemId).isEqualTo(cartItem.getId());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("장바구니 아이템 조회 테스트")
    void findCartItemServiceTest() {
        Long memberId = 1L;
        Member member = createTestMember(memberId);
        Cart cart = createTestCart(member);
        List<CartItem> cartItems = List.of(
                createTestCartItem(cart, createTestItem(1L), 2),
                createTestCartItem(cart, createTestItem(2L), 1)
        );

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(cartRepository.findByMember(member))
                .willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByCartOrderByCreatedAt(cart))
                .willReturn(cartItems);

        FindCartItemsResponse response = cartService.findCartItemsByMemberId(memberId);

        assertThat(response.cartItems()).hasSize(2);
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("test@email.com")
                .nickname("tester")
                .build();
    }

    private Cart createTestCart(Member member) {
        return Cart.builder()
                .id(1L)
                .member(member)
                .build();
    }

    private CartItem createTestCartItem(Cart cart, Item item, int quantity) {
        return CartItem.builder()
                .id(1L)
                .cart(cart)
                .item(item)
                .quantity(quantity)
                .build();
    }

    private Item createTestItem(Long itemId) {
        return Item.builder()
                .id(itemId)
                .name("Test Item")
                .price(10000)
                .build();
    }
}
