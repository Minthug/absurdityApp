package forOlderJava.absurdityAppForJava.domain.order.controller;

import forOlderJava.absurdityAppForJava.domain.order.exception.OrderException;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.request.UpdateOrderByCouponCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.response.*;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody final CreateOrderRequest createOrderRequest,
                                                           @LoginUser final Long memberId) {
        CreateOrdersCommand createOrdersCommand = CreateOrdersCommand.of(memberId, createOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createOrdersCommand));
    }

    @PostMapping("/{orderId}/apply-coupon")
    public ResponseEntity<UpdateOrderByCouponResponse> updateOrderByCoupon(@PathVariable final Long orderId,
                                                                           @LoginUser Long memberId,
                                                                           @RequestParam final Long couponId) {

        UpdateOrderByCouponCommand updateOrderByCouponCommand = UpdateOrderByCouponCommand.of(orderId, memberId, couponId);

        return ResponseEntity.ok(orderService.updateOrderByCoupon(updateOrderByCouponCommand));

    }

    @GetMapping
    public ResponseEntity<FindOrdersResponse> findOrders(@RequestParam(defaultValue = "0") Integer page,
                                                         @LoginUser final Long memberId) {
        return ResponseEntity.ok(orderService.findOrders(memberId, page));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDetailResponse> findOrderByIdAndMemberId(@PathVariable final Long orderId,
                                                                            @LoginUser final Long memberId) {
        return ResponseEntity.ok(orderService.findOrderByIdAndMemberId(orderId, memberId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable final Long orderId,
                                            @LoginUser final Long memberId) {
        orderService.deleteOrder(orderId, memberId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorTemplate> handleException(final OrderException orderException) {
        log.error(orderException.getMessage());
        return ResponseEntity.badRequest().body(ErrorTemplate.of(orderException.getMessage()));
    }
}
