package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

public record CreatePostDto(String title, String content, UserDto author) {
}
