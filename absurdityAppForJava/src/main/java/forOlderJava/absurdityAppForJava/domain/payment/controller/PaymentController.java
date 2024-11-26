package forOlderJava.absurdityAppForJava.domain.payment.controller;

import forOlderJava.absurdityAppForJava.domain.payment.service.PaymentClient;
import forOlderJava.absurdityAppForJava.domain.payment.service.PaymentService;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentRequestResponse;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentResponse;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestControllerAdvice
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pays")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentClient paymentClient;


    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentRequestResponse> pay(@PathVariable Long orderId, @LoginUser Long memberId) {

        return ResponseEntity.ok(paymentService.pay(orderId, memberId));
    }

    @GetMapping("/toss/success")
    public ResponseEntity<PaymentResponse> paySuccess(@RequestParam("orderId") String uuid,
                                                      @RequestParam("paymentKey") String paymentKey,
                                                      @RequestParam("amount") Integer amount,
                                                      @LoginUser Long memberId) {

        paymentClient.confirmPayment(uuid, paymentKey, amount);

        return ResponseEntity.ok(paymentService.processSuccessPayment(memberId, uuid, paymentKey, amount));
    }

    @GetMapping("/toss/fail")
    public ResponseEntity<PaymentResponse> payFail(@RequestParam("orderId") String uuid,
                                                   @RequestParam("message") String errorMessage,
                                                   @LoginUser Long memberId) {
        return ResponseEntity.ok(paymentService.processFailPayment(memberId, uuid, errorMessage));
    }
}
