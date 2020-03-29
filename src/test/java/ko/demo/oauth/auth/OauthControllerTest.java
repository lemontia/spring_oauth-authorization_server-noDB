package ko.demo.oauth.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.message.ObjectMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class OauthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("oauth token 테스트")
    void oauthTokenTest() throws Exception {
        // given
        String clientId = "testClientId";
        String secret = "testSecret";
        String username = "user";
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", "pass");


        // when
        MvcResult result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, secret))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        System.out.println("result = " + result);
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("body = " + contentAsString);

        // then
        Assertions.assertThat(contentAsString).contains("access_token").contains("refresh_token");

        Map map = objectMapper.readValue(contentAsString, Map.class);
        System.out.println("map = " + map);


        // token 검토
        String accessToken = (String) map.get("access_token");
        String url = "/oauth/check_token?token=" + accessToken;

        MvcResult mvcResultApi = mockMvc.perform(get(url)).andReturn();

        System.out.println("mvcResultApi = " + mvcResultApi);
        System.out.println("mvcResultApi.getResponse().getContentAsString() = " + mvcResultApi.getResponse().getContentAsString());
        String checkTokenStr = mvcResultApi.getResponse().getContentAsString();

        Map checkTokenMap = objectMapper.readValue(checkTokenStr, Map.class);
        boolean active = (boolean) checkTokenMap.get("active");;
        String user_name = (String) checkTokenMap.get("user_name");
        String client_id = (String) checkTokenMap.get("client_id");

        Assertions.assertThat(active).isEqualTo(true);
        Assertions.assertThat(user_name).isEqualTo(username);
        Assertions.assertThat(client_id).isEqualTo(clientId);
    }
}