package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public interface AuthorizeInboundPort {

    record CanCreatePostInputDto(CreatePostDto post, UserDto requestUser) {
    }

    Either<String, Boolean> canCreatePost(CanCreatePostInputDto inputDto);

}
