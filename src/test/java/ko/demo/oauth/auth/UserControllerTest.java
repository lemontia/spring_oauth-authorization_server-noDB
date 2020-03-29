package ko.demo.oauth.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("access_token 없이 접근하기")
    void noAccessTokenTest() throws Exception {
        String url = "/api/me";

        MvcResult mvcResult = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assertions.assertThat(mvcResult.getResponse().getStatus())
                .isEqualTo(401);
        Assertions.assertThat(mvcResult.getResponse().getContentAsString())
                .contains("unauthorized");
    }

    @Test
    @DisplayName("access_token 으로 접근하기")
    void accessTokenTest() throws Exception {
        // access_token 획득
        String clientId = "testClientId";
        String secret = "testSecret";
        String credentials = clientId+":"+secret;
        String encodedCredentials = new String(Base64.encode(credentials.getBytes()));

        TestUser testUser = new TestUser();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", testUser.getUsername());
        params.add("password", testUser.getPassword());
        params.add("grant_type", "password");

        MvcResult mvcResult = mockMvc.perform(post("/oauth/token")
                .params(params)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic " + encodedCredentials)
        ).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(response).contains("access_token");
        Assertions.assertThat(response).contains("read write");

        Map map = objectMapper.readValue(response, Map.class);
        System.out.println("map = " + map);


        // token 으로 접근

        String accessToken = (String) map.get("access_token");
        String url = "/api/me";

        MvcResult mvcResultApi = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer " + accessToken)
        ).andReturn();

        System.out.println("mvcResultApi = " + mvcResultApi);
        System.out.println("mvcResultApi.getResponse().getContentAsString() = " + mvcResultApi.getResponse().getContentAsString());
        String responseString = mvcResultApi.getResponse().getContentAsString();

        Assertions.assertThat(mvcResultApi.getResponse().getStatus()).isEqualTo(200);
        Assertions.assertThat(responseString).isEqualTo(testUser.getUsername());
    }
}