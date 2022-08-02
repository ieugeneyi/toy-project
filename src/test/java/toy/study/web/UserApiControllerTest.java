package toy.study.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toy.study.domain.user.Role;
import toy.study.domain.user.User;
import toy.study.domain.user.UserRepository;
import toy.study.web.dto.UserSaveRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void User_생성된다(){

        //given
        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test"))
                .role(Role.USER)
                .build();

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    public void User_생성_api호출() throws Exception{
        //given
        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .name("test2")
                .email("test2@gmail.com")
                .password(passwordEncoder.encode("test"))
                .role(Role.USER)
                .build();
        String url = "http://localhost:" + port  + "/api/v1/users";

        //when
        mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getEmail()).isEqualTo(requestDto.getEmail());
    }



}