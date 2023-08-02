package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignInInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.UserOutputDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.CheckPasswordOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodeJwtOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Email;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SignInService implements SignInInboundPort {

    private final CheckPasswordOutboundPort checkPasswordOutboundPort;

    private final EncodeJwtOutboundPort encodeJwtOutboundPort;

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);

    public SignInService(
            CheckPasswordOutboundPort checkPasswordOutboundPort,
            EncodeJwtOutboundPort encodeJwtOutboundPort,
            UserRepository userRepository) {
        this.checkPasswordOutboundPort = checkPasswordOutboundPort;
        this.encodeJwtOutboundPort = encodeJwtOutboundPort;
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorCode, OutputDto> signIn(InputDto inputDto) {
        var emailResult = Email.create(inputDto.email());
        if (emailResult.isLeft()) {
            var errorCode = emailResult.left();
            if (errorCode == Email.ErrorCode.NO_AT) {
                return new Either.Left<>(ErrorCode.ILLEGAL_EMAIL_NO_AT);
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new RuntimeException("잘못된 이메일 주소입니다.");
        }
        var email = emailResult.right();

        var userResult = this.userRepository.findByEmail(email.email());
        if (userResult.isEmpty()) {
            return new Either.Left<>(ErrorCode.ILLEGAL_EMAIL_NOT_SIGNED_UP);
        }
        var user = userResult.get();

        var passwordResult = Password.create(inputDto.password());
        if (passwordResult.isLeft()) {
            var errorCode = passwordResult.left();
            if (errorCode == Password.ErrorCode.TOO_SHORT) {
                return new Either.Left<>(ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT);
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }
        var password = passwordResult.right();

        if (!this.checkPasswordOutboundPort.check(password.password(), user.password())) {
            return new Either.Left<>(ErrorCode.ILLEGAL_PASSWORD_WRONG);
        }

        var token = this.encodeJwtOutboundPort.encode(
                new EncodeJwtOutboundPort.InputDto(user.id(), new Date()));
        return new Either.Right<>(new OutputDto(new UserOutputDto(user.id(), user.email()), token));
    }

}
