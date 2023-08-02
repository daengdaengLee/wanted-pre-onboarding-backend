package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final SignUpInboundPort signUpInboundPort;

    public UserController(SignUpInboundPort signUpInboundPort) {
        this.signUpInboundPort = signUpInboundPort;
    }

    public record AuthInputDto(String email, String password) {
    }

    public record UserOutputDto(Long id, String email) {
    }

    public record SignUpOutputDto(UserOutputDto user) {
    }

    @PostMapping("sign-up")
    public SignUpOutputDto signUp(@RequestBody AuthInputDto authInputDto) {
        var signUpInputDto = new SignUpInboundPort.InputDto(authInputDto.email(), authInputDto.password());
        var signUpOutputDto = this.signUpInboundPort.signUp(signUpInputDto);
        return new SignUpOutputDto(new UserOutputDto(signUpOutputDto.id(), signUpOutputDto.email()));
    }

    public record SignInOutputDto(UserOutputDto user, String token) {
    }

    @PostMapping("sign-in")
    public SignInOutputDto signIn(@RequestBody AuthInputDto authInputDto) {
        throw new RuntimeException("not implemented");
    }

}
