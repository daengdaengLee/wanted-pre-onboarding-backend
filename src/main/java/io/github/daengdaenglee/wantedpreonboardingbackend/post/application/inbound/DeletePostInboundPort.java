package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

public interface DeletePostInboundPort {

    record InputDto(Long id) {
    }

    void deletePost(InputDto inputDto);

}
