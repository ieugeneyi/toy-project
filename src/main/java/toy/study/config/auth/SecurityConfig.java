package toy.study.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import toy.study.domain.user.Role;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/js/**", "/images/**", "/fonts/**",
                            "/vendors/**", "/scss/**", "/h2-console/**", "/login", "/register").permitAll()
                    .antMatchers("/api/v1/users/**").permitAll()
                    .antMatchers("/api/v1/posts/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/loginProcess")
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureHandler(failureHandler())
                .and()
                    .rememberMe()
                    .rememberMeParameter("remember")
                    .userDetailsService(userDetailsService)
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                .and()
                    .oauth2Login()
                        .failureHandler(failureHandler())
                        .loginPage("/login")
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);



        return http.build();
    }


    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new CustomAuthFailureHandler();
    }

}
