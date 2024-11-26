package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.payment.controller.PaymentController;
import forOlderJava.absurdityAppForJava.domain.payment.service.PaymentClient;
import forOlderJava.absurdityAppForJava.domain.payment.service.PaymentService;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentRequestResponse;
import forOlderJava.absurdityAppForJava.global.auth.jwt.JwtAuthenticationProvider;
import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.apache.zookeeper.ZooDefs.OpCode.auth;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentClient paymentClient;

    @MockBean
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @DisplayName("1. 결제 요청 테스트")
    void payTest() throws Exception {
        Long orderId = 1L;
        Long memberId = 1L;

        JwtAuthentication principal = new JwtAuthentication(memberId, "test-access-token");
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_OLDER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        PaymentRequestResponse expectedResponse = PaymentRequestResponse.builder()
                .amount(10000)
                .orderId("test-order-uuid")
                .orderName("Test Order")
                .customerEmail("test@email.com")
                .customerName("Tester")
                .successUrl("http://localhost:8080/v1/pays/toss/success")
                .failUrl("http://localhost:8080/v1/pays/toss/fail")
                .build();

        given(paymentService.pay(anyLong(), anyLong()))
                .willReturn(expectedResponse);

        mockMvc.perform(post("/v1/pays/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedResponse.amount()))
                .andExpect(jsonPath("$.orderId").value(expectedResponse.orderId()))
                .andExpect(jsonPath("$.orderName").value(expectedResponse.orderName()))
                .andDo(print());

    }
}
