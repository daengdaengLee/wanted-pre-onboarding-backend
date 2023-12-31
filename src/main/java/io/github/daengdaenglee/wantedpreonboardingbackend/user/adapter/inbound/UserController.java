package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.SimpleApiException;
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

        public void validate() {
            if (this.email() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "이메일 주소를 입력해주세요.");
            }

            if (this.password() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요.");
            }
        }

    }

    public record UserOutputDto(String id, String email) {
    }

    public record SignUpOutputDto(UserOutputDto user) {
    }

    @PostMapping("sign-up")
    public SignUpOutputDto signUp(@RequestBody AuthInputDto authInputDto) {
        authInputDto.validate();

        var signUpInputDto = new SignUpInboundPort.InputDto(authInputDto.email(), authInputDto.password());
        var signUpOutputDtoResult = this.signUpInboundPort.signUp(signUpInputDto);
        if (signUpOutputDtoResult.isLeft()) {
            var errorCode = signUpOutputDtoResult.left();
            if (errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_NO_AT ||
                    errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, errorCode.message());
            } else if (errorCode == SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_DUPLICATED) {
                throw new SimpleApiException(HttpStatus.CONFLICT, errorCode.message());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
        }
        var signUpOutputDto = signUpOutputDtoResult.right();
        return new SignUpOutputDto(new UserOutputDto(
                signUpOutputDto.id().toString(),
                signUpOutputDto.email()));
    }

    public record SignInOutputDto(UserOutputDto user, String token) {
    }

    @PostMapping("sign-in")
    public SignInOutputDto signIn(@RequestBody AuthInputDto authInputDto) {
        authInputDto.validate();

        var signInInputDto = new SignInInboundPort.InputDto(authInputDto.email(), authInputDto.password());
        var signInOutputDtoResult = this.signInInboundPort.signIn(signInInputDto);
        if (signInOutputDtoResult.isLeft()) {
            var errorCode = signInOutputDtoResult.left();
            if (errorCode == SignInInboundPort.ErrorCode.ILLEGAL_EMAIL_NO_AT ||
                    errorCode == SignInInboundPort.ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, errorCode.message());
            } else if (errorCode == SignInInboundPort.ErrorCode.ILLEGAL_EMAIL_NOT_SIGNED_UP ||
                    errorCode == SignInInboundPort.ErrorCode.ILLEGAL_PASSWORD_WRONG) {
                throw new SimpleApiException(HttpStatus.UNAUTHORIZED, errorCode.message());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
        }
        var signInOutputDto = signInOutputDtoResult.right();
        return new SignInOutputDto(
                new UserOutputDto(
                        signInOutputDto.user().id().toString(),
                        signInOutputDto.user().email()),
                signInOutputDto.token());
    }

}
