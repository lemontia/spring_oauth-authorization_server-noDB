package ko.demo.oauth.config.oauth2.authorization;

import ko.demo.oauth.auth.TestUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * DB에서 유저정보를 조회하여 유효성 검토(비밀번호 체크 등)
 */
@Configuration
public class OauthAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        System.out.println("authentication.username = " + username);
        System.out.println("authentication.password = " + password);

        // 테스트 유저 호출(만약 DB에 연동해서 불러온다면 대체해도 된다)
        TestUser testUser = new TestUser();

        if(password.equals(testUser.getPassword()) == false) {
            throw new BadCredentialsException(username);
        }

        return new UsernamePasswordAuthenticationToken(username, password, testUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
