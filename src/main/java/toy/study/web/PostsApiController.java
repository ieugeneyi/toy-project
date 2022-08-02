package toy.study.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toy.study.service.posts.PostsService;
import toy.study.web.dto.PostsSaveRequestDto;
import toy.study.web.dto.ResultResponseDto;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public ResultResponseDto save(@RequestBody PostsSaveRequestDto requestDto){
        try{
            postsService.save(requestDto);
        }catch (Exception e){
            return new ResultResponseDto("error", e.getMessage());
        }
        return new ResultResponseDto("success", "save success");
    }

    @PutMapping("/api/v1/posts/{id}")
    public ResultResponseDto update(@PathVariable Long id, @RequestBody PostsSaveRequestDto requestDto){
        try {
            postsService.update(id, requestDto);
        }catch (Exception e){
            return new ResultResponseDto("error", e.getMessage());
        }
        return new ResultResponseDto("success", "update success");
    }

}
