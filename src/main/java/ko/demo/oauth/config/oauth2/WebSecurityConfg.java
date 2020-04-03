package ko.demo.oauth.config.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfg extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                    .antMatchers("/oauth/authorize").permitAll()
                    .antMatchers("/oauth/**", "/oauth2/callback", "/h2-console/*").permitAll()
                .and()
                .formLogin().and()
                .httpBasic()
                ;
    }


}
