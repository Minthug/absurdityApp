package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.service.MemberService;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.younger.service.ErrandService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderFlowIntegrationTest {

    private MemberService memberService;
    private OrderService orderService;
    private ErrandService errandService;
}
