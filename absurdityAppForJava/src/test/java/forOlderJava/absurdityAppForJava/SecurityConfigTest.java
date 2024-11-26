package forOlderJava.absurdityAppForJava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("1. 인증이 필요없는 API 테스트")
    void permitAllApisTest() throws Exception {
        mockMvc.perform(get("/v1/events"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("2. ROLE_OLDER 권한이 필요한 API 테스트")
    void permitOlderApisTest() throws Exception {
        mockMvc.perform(get("/v1/members/me"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
