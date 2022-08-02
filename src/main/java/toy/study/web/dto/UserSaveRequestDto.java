package toy.study.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.multipart.MultipartFile;
import toy.study.domain.user.Role;
import toy.study.domain.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    @NotBlank(message = "NAME_IS_MANDATORY")
    @Size(min=2, max=30)
    private String name;

    @NotBlank(message = "EMAIL_IS_MANDATORY")
    @Email(message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    @Builder
    public UserSaveRequestDto(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }


}
