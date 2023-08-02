package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.UserOutputDto;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements SignUpInboundPort {

    @Override
    public UserOutputDto signUp(InputDto inputDto) {
        throw new RuntimeException("not implemented");
    }

}
