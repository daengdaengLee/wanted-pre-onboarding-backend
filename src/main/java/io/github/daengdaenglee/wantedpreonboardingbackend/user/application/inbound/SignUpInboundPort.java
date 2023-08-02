package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

public interface SignUpInboundPort {

    record InputDto(String email, String password) {
    }

    UserOutputDto signUp(InputDto inputDto);

}
