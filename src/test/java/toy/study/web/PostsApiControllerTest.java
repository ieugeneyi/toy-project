package toy.study.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toy.study.domain.posts.Posts;
import toy.study.domain.posts.PostsRepository;
import toy.study.domain.user.Role;
import toy.study.domain.user.User;
import toy.study.domain.user.UserRepository;
import toy.study.web.dto.PostsSaveRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    WebApplicationContext context;

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
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_등록() throws Exception{
        //given
        String title = "title";
        String content = "content";

        User user = User.builder()
                .name("tester")
                .email("tester@naver.com")
                .role(Role.USER)
                .build();

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                    .title(title)
                                                    .content(content)
                                                    .user(user)
                                                    .build();
        String url = "http://localhost:" + port  + "/api/v1/posts";



        //when
        mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getUser().getName()).isEqualTo(user.getName());

    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_수정() throws Exception{
        //given
        User user = User.builder()
                .name("tester")
                .email("tester@gmail.com")
                .role(Role.USER)
                .build();
        Posts saved = postsRepository.save(Posts.builder()
                                        .title("title")
                                        .content("content")
                                        .user(user)
                                        .build());

        Long updateId = saved.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                            .title(expectedTitle)
                                            .content(expectedContent)
                                            .user(saved.getUser())
                                            .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //when
        mvc.perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());


        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }


}