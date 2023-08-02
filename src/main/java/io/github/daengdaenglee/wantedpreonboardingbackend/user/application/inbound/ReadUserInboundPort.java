package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

import java.util.Optional;

public interface ReadUserInboundPort {

    record InputDto(Long id) {
    }

    Optional<UserOutputDto> readUser(InputDto inputDto);

}
