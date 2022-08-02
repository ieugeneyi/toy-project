package toy.study.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.study.config.auth.dto.CustomUser;
import toy.study.config.auth.dto.SessionUser;
import toy.study.domain.user.User;
import toy.study.domain.user.UserRepository;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not exist"));

        httpSession.setAttribute("user", new SessionUser(user));

        return new CustomUser(user);
    }



}
