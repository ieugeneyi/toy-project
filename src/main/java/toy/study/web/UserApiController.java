package toy.study.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.study.service.user.UserService;
import toy.study.web.dto.ResultResponseDto;
import toy.study.web.dto.UserSaveRequestDto;


@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ResultResponseDto save(@Validated @RequestBody UserSaveRequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResultResponseDto("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            userService.save(requestDto);
        }catch (Exception e){
            return new ResultResponseDto("error", e.getMessage());
        }
        return new ResultResponseDto("success", "register success");
    }

}
