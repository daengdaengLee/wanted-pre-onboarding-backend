package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.UserOutputDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodePasswordOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Email;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.EncodedPassword;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Password;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements SignUpInboundPort {

    private final EncodePasswordOutboundPort encodePasswordOutboundPort;

    private final UserRepository userRepository;

    public SignUpService(
            EncodePasswordOutboundPort encodePasswordOutboundPort,
            UserRepository userRepository) {
        this.encodePasswordOutboundPort = encodePasswordOutboundPort;
        this.userRepository = userRepository;
    }

    @Override
    public UserOutputDto signUp(InputDto inputDto) {
        var emailResult = Email.create(inputDto.email());
        if (emailResult.isLeft()) {
            // @TODO
            throw new RuntimeException("잘못된 이메일 주소");
        }
        var email = emailResult.right();

        if (this.userRepository.findByEmail(email.email()).isPresent()) {
            // @TODO
            throw new RuntimeException("이미 사용 중인 이메일");
        }

        var passwordResult = Password.create(inputDto.password());
        if (passwordResult.isLeft()) {
            // @TODO
            throw new RuntimeException("잘못된 비밀번호");
        }
        var password = passwordResult.right();
        var encodedPassword = new EncodedPassword(
                this.encodePasswordOutboundPort.encode(password.password()));

        var user = this.userRepository.save(new User(email, encodedPassword));

        return new UserOutputDto(user.id(), user.email());
    }

}
