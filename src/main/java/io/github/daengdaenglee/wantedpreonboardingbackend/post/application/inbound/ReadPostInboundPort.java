package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import java.util.Optional;

public interface ReadPostInboundPort {

    record InputDto(Long id) {
    }

    Optional<PostDto> readPost(InputDto inputDto);

}
