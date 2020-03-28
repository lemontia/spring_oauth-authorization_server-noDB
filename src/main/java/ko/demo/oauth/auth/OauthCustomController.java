//package ko.demo.oauth.auth;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.support.SessionStatus;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//public class OauthCustomController extends AuthorizationEndpoint {
//
//    @RequestMapping(value = "/oauth/authorize")
//    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
//                                  SessionStatus sessionStatus, Principal principal) {
//
//
//        System.out.println("===================");
//
//        return null;
//    }
//}
