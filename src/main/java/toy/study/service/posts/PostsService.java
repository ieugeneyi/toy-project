package toy.study.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.study.domain.posts.Posts;
import toy.study.domain.posts.PostsRepository;
import toy.study.web.dto.PostsSaveRequestDto;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsSaveRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("posts not exists id="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }



}
