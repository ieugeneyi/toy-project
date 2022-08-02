package toy.study.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import toy.study.config.auth.LoginUser;
import toy.study.config.auth.dto.SessionUser;
import toy.study.domain.user.User;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model,
                        @LoginUser SessionUser user){
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }
}
