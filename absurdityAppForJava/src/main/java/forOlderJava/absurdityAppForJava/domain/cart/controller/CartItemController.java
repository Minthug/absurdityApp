package forOlderJava.absurdityAppForJava.domain.cart.controller;

import forOlderJava.absurdityAppForJava.domain.cart.exception.CartItemException;
import forOlderJava.absurdityAppForJava.domain.cart.service.CartService;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.RegisterCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.RegisterCartItemRequest;
import forOlderJava.absurdityAppForJava.domain.cart.service.request.UpdateCartItemCommand;
import forOlderJava.absurdityAppForJava.domain.cart.service.response.FindCartItemResponse;
import forOlderJava.absurdityAppForJava.domain.cart.service.response.FindCartItemsResponse;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/cartItems")
@RequiredArgsConstructor
@Slf4j
public class CartItemController {
    private final CartService cartService;
    private static final String BASE_URI = "/v1/cartItems/";

    @PostMapping
    public ResponseEntity<Void> registerCartItem(@Valid @RequestBody RegisterCartItemRequest request,
                                                 @LoginUser final Long memberId) {

        RegisterCartItemCommand registerCartItemCommand = RegisterCartItemCommand.of(memberId, request.itemId(), request.quantity());

        Long cartItemId = cartService.registerCartItem(registerCartItemCommand);
        URI location = URI.create(BASE_URI + cartItemId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable final Long cartItemId) {

        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-cart-items")
    public ResponseEntity<FindCartItemsResponse> findCartItemByMemberId(@LoginUser final Long memberId) {
        return ResponseEntity.ok().body(cartService.findCartItemsByMemberId(memberId));
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable final Long cartItemId,
                                                       @Valid @RequestBody final int quantity) {
        UpdateCartItemCommand updateCartItemCommand = UpdateCartItemCommand.of(cartItemId, quantity);

        cartService.updateCartItemQuantity(updateCartItemCommand);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<ErrorTemplate> handleException(final CartItemException cartItemException) {
        log.info(cartItemException.getMessage());
        return ResponseEntity.badRequest().body(ErrorTemplate.of(cartItemException.getMessage()));
    }
}
