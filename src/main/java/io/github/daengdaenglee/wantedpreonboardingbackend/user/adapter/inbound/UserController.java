package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.inbound;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    public record SignUpInputDto(String email, String password) {
    }

    public record UserOutputDto(Long id, String email) {
    }

    public record SignUpOutputDto(UserOutputDto user) {
    }

    @PostMapping("sign-up")
    public SignUpOutputDto signUp(@RequestBody SignUpInputDto signUpInputDto) {
        throw new RuntimeException("not implemented");
    }

}
