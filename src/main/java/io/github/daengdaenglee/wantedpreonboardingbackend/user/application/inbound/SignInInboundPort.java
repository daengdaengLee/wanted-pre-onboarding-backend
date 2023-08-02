package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

public interface SignInInboundPort {

    record InputDto(String email, String password) {
    }

    record OutputDto(UserOutputDto user, String token) {
    }

    OutputDto signIn(InputDto inputdto);

}
