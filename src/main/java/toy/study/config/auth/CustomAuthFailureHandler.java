package toy.study.config.auth;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Getter
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error=true&exception=";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException){
            errorMessage = "Invalid Username or Password";
        }else{
            errorMessage = exception.getMessage();
        };
        errorMessage= URLEncoder.encode(errorMessage,"UTF-8");
        setDefaultFailureUrl(DEFAULT_FAILURE_URL + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
