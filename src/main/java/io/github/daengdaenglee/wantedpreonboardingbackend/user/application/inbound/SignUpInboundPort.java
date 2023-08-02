package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

public interface SignUpInboundPort {

    record InputDto(String email, String password) {
    }

    record OutputDto(Long id, String email) {
    }

    OutputDto signUp(InputDto inputDto);

}
