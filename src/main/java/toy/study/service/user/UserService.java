package toy.study.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.study.domain.user.Role;
import toy.study.domain.user.User;
import toy.study.domain.user.UserRepository;
import toy.study.web.dto.UserSaveRequestDto;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User save(UserSaveRequestDto requestDto){
        boolean present = userRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (present){
            throw new IllegalArgumentException("already exists");
        }

        //암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

}
