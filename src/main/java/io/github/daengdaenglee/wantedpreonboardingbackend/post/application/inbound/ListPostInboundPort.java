package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import java.util.List;
import java.util.Optional;

public interface ListPostInboundPort {

    record InputDto(Optional<Long> cursor, Integer count) {
    }

    record OutputDto(List<PostDto> posts, Boolean hasNext) {
    }

    OutputDto listPost(InputDto inputDto);

}
