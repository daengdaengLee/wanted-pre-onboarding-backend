package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.AuthorizeInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.ReadPostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements AuthorizeInboundPort {

    private final ReadPostInboundPort readPostInboundPort;

    public AuthorizeService(ReadPostInboundPort readPostInboundPort) {
        this.readPostInboundPort = readPostInboundPort;
    }

    @Override
    public Either<CanCreatePostErrorCode, Boolean> canCreatePost(CanCreatePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanCreatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Override
    public Either<CanUpdatePostErrorCode, Boolean> canUpdatePost(CanUpdateOrDeletePostInputDto inputDto) {
        var currentPostResult = this.readPostInboundPort.readPost(
                new ReadPostInboundPort.InputDto(inputDto.postId()));
        if (currentPostResult.isEmpty()) {
            return new Either.Left<>(CanUpdatePostErrorCode.NOT_EXIST);
        }
        var currentPost = currentPostResult.get();

        if (this.isSameUser(currentPost.author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanUpdatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Override
    public Either<CanDeletePostErrorCode, Boolean> canDeletePost(CanUpdateOrDeletePostInputDto inputDto) {
        var currentPostResult = this.readPostInboundPort.readPost(
                new ReadPostInboundPort.InputDto(inputDto.postId()));
        if (currentPostResult.isEmpty()) {
            return new Either.Left<>(CanDeletePostErrorCode.NOT_EXIST);
        }
        var currentPost = currentPostResult.get();

        if (this.isSameUser(currentPost.author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanDeletePostErrorCode.ILLEGAL_AUTHOR);
    }

    private boolean isSameUser(UserDto user1, UserDto user2) {
        return user1.id().longValue() == user2.id().longValue();
    }

}
