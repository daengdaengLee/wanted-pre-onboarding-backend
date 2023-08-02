package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

public record PostDto(Long id, String title, String content, UserDto author) {
}
