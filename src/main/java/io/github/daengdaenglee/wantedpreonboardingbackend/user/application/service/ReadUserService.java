package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.ReadUserInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.UserOutputDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReadUserService implements ReadUserInboundPort {

    private final UserRepository userRepository;

    public ReadUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserOutputDto> readUser(InputDto inputDto) {
        return this.userRepository
                .findById(inputDto.id())
                .map(user -> new UserOutputDto(user.id(), user.email()));
    }

}
