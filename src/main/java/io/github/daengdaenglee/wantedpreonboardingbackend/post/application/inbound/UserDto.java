package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public record UserDto(Long id) {

    public static Either<String, UserDto> create(String id) {
        try {
            var userDto = new UserDto(Long.valueOf(id));
            return new Either.Right<>(userDto);
        } catch (Exception exception) {
            var message = exception.getMessage();
            return new Either.Left<>(message == null ? "잘못된 유저 아이디입니다." : message);
        }
    }

}
