package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.UserOutputDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodePasswordOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Email;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.EncodedPassword;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Password;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements SignUpInboundPort {

    private final EncodePasswordOutboundPort encodePasswordOutboundPort;

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);

    public SignUpService(
            EncodePasswordOutboundPort encodePasswordOutboundPort,
            UserRepository userRepository) {
        this.encodePasswordOutboundPort = encodePasswordOutboundPort;
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorCode, UserOutputDto> signUp(InputDto inputDto) {
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

        if (this.userRepository.findByEmail(email.email()).isPresent()) {
            return new Either.Left<>(ErrorCode.ILLEGAL_EMAIL_DUPLICATED);
        }

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
        var encodedPassword = new EncodedPassword(
                this.encodePasswordOutboundPort.encode(password.password()));

        var user = this.userRepository.save(new User(email, encodedPassword));

        return new Either.Right<>(new UserOutputDto(user.id(), user.email()));
    }

}
