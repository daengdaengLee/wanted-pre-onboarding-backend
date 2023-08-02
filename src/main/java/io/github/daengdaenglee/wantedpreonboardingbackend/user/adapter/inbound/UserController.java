package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignInInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final SignUpInboundPort signUpInboundPort;

    private final SignInInboundPort signInInboundPort;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(
            SignUpInboundPort signUpInboundPort,
            SignInInboundPort signInInboundPort) {
        this.signUpInboundPort = signUpInboundPort;
        this.signInInboundPort = signInInboundPort;
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
        var signUpOutputDtoResult = this.signUpInboundPort.signUp(signUpInputDto);
        if (signUpOutputDtoResult.isLeft()) {
            var errorCode = signUpOutputDtoResult.left();
            if (errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_NO_AT) {
                // @TODO
                throw new RuntimeException(HttpStatus.BAD_REQUEST.name());
            } else if (errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_DUPLICATED) {
                // @TODO
                throw new RuntimeException(HttpStatus.CONFLICT.name());
            } else if (errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT) {
                // @TODO
                throw new RuntimeException(HttpStatus.BAD_REQUEST.name());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            // @TODO
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        var signUpOutputDto = signUpOutputDtoResult.right();
        return new SignUpOutputDto(new UserOutputDto(signUpOutputDto.id(), signUpOutputDto.email()));
    }

    public record SignInOutputDto(UserOutputDto user, String token) {
    }

    @PostMapping("sign-in")
    public SignInOutputDto signIn(@RequestBody AuthInputDto authInputDto) {
        var signInInputDto = new SignInInboundPort.InputDto(authInputDto.email(), authInputDto.password());
        var signInOutputDto = this.signInInboundPort.signIn(signInInputDto);
        return new SignInOutputDto(
                new UserOutputDto(
                        signInOutputDto.user().id(),
                        signInOutputDto.user().email()),
                signInOutputDto.token());
    }

}
