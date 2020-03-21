package ko.demo.oauth.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OauthTokenDtoTest {

    @Test
    @DisplayName("objectMapper convert 테스트")
    void convertTest() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"access_token\":\"1c4bce4f-7cdd-4e5f-b3cc-3a155990c8fb\",\"token_type\":\"bearer\",\"expires_in\":43199,\"scope\":\"read write\"}";

        // when
        OauthController.OauthTokenDto oauthTokenDto = objectMapper.readValue(json, OauthController.OauthTokenDto.class);

        // then
        System.out.println("oauthTokenDto = " + oauthTokenDto.toString());
    }
}