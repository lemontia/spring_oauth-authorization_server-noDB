package ko.demo.oauth.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("/api/me")
    public String apiMe(Principal principal) {
        System.out.println("principal = " + principal);

        if(principal == null) {
            return "pricipal is null";
        }
        return principal.getName();
    }
}
