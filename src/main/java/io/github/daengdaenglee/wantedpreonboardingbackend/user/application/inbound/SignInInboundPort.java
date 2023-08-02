package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

public interface SignInInboundPort {

    record InputDto(String email, String password) {
    }

    record UserOutputDto(Long id, String email) {
    }

    record OutputDto(UserOutputDto user, String token) {
    }

    OutputDto signIn(InputDto inputdto);

}
