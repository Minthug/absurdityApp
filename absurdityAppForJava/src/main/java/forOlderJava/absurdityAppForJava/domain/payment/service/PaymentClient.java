package forOlderJava.absurdityAppForJava.domain.payment.service;

import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentFailException;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.TossPaymentApiResponse;
import forOlderJava.absurdityAppForJava.global.infrastructure.ApiService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final ApiService apiService;

    private String secretKey;

    private String confirmUrl;

    public void confirmPayment(final String uuid, final String paymentKey, final Integer amount) {
        HttpHeaders httpHeaders = getHttpHeaders();
        JSONObject params = getParams(uuid, paymentKey, amount);
        TossPaymentApiResponse paymentAPiResponse = requestPaymentApi(httpHeaders, params);

        validatePaymentResult(paymentAPiResponse);

    }

    private JSONObject getParams(String uuid, String paymentKey, Integer amount) {
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", uuid);
        params.put("amount", amount);

        return params;
    }

    private void validatePaymentResult(TossPaymentApiResponse paymentAPiResponse) {
        if (!paymentAPiResponse.status().equals("DONE")) {
            throw new PaymentFailException("결제가 실패되었습니다");
        }
    }

    private TossPaymentApiResponse requestPaymentApi(HttpHeaders httpHeaders, JSONObject params) {
        return apiService.getResult(
                new HttpEntity<>(params, httpHeaders),
                confirmUrl,
                TossPaymentApiResponse.class
        );
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(getEncodeAuth());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    private String getEncodeAuth() {
        return new String(
                Base64.getEncoder()
                        .encode((secretKey + ":").getBytes(StandardCharsets.UTF_8))
        );
    }
}
