package ko.demo.oauth.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class CorsFilterTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;
    @Value("${local.server.port:8080}")
    int localServerPort;


    @Test
    @DisplayName("CORS(options) 테스트")
    void corsOptionTest() throws Exception {
        // given
        String url = "/oauth/token";
        String credentials = "testClientId:testSecret";
        String encodedCredentials = new String(Base64.encode(credentials.getBytes()));

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type", "password");
        params.add("username", "user");
        params.add("password", "pass");
        params.add("scope", "read write");


        // when
        MvcResult mvcResult = mockMvc.perform(options(url)
                .header("Access-Control-Request-Method", "OPTIONS")
                .header("Origin", "http://localhost:3000")
//                .header("Authorization", "Basic " + encodedCredentials)
//                .params(params)
                )
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertThat(status).isEqualTo(200);

        // when
        MvcResult mvcResult2 = mockMvc.perform(post(url)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://localhost:3000")
//                .header("Authorization", "Basic " + encodedCredentials)
//                .params(params)
        )
                .andReturn();

        int status2 = mvcResult2.getResponse().getStatus();
        Assertions.assertThat(status2).isEqualTo(401);
    }


    @Test
    @DisplayName("CORS Filter 테스트")
    void corsFilterTest() throws Exception {
        // given
        String url = "/oauth/token";

        // when - option 전송
        MvcResult mvcResultOption = mockMvc.perform(options(url)
                        .header("Access-Control-Request-Method", "OPTIONS")
                        .header("Origin", "http://localhost:3000")
        )
                .andReturn();

        int statusOption = mvcResultOption.getResponse().getStatus();
        Assertions.assertThat(statusOption).isEqualTo(200);

        // when - post 전송
        MvcResult mvcResultPost = mockMvc.perform(post(url)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://localhost:3000")
        )
                .andReturn();

        int statusPost = mvcResultPost.getResponse().getStatus();
        Assertions.assertThat(statusPost).isEqualTo(401);


        System.out.println("=========================");
        System.out.println("option status => " + statusOption);
        System.out.println("post status => " + statusPost);
        System.out.println("=========================");
    }



    @Test
    @DisplayName("Oauth2 Token get 테스트")
    void oauthTokenTest() {
        String clientId = "testClientId";
        String secret = "testSecret";
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type", "password");
        params.add("username", "user");
        params.add("password", "pass");
        params.add("scope", "read write");

        String url = "http://localhost:" + localServerPort + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("http://localhost:3000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, secret);

        HttpEntity req = new HttpEntity(params, headers);
        ;

        //        mockMvc.perform(post(url))
        ResponseEntity<String> res = restTemplate.postForEntity(url, req, String.class);
        System.out.println("stringResponseEntity.getBody() = " + res.getBody());


    }
}
