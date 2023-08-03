package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.AuthorizeInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements AuthorizeInboundPort {

    @Override
    public Either<CanCreatePostErrorCode, Boolean> canCreatePost(CanCreatePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanCreatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Override
    public Either<CanUpdatePostErrorCode, Boolean> canUpdatePost(CanUpdateOrDeletePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanUpdatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Override
    public Either<CanDeletePostErrorCode, Boolean> canDeletePost(CanUpdateOrDeletePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>(CanDeletePostErrorCode.ILLEGAL_AUTHOR);
    }

    private boolean isSameUser(UserDto user1, UserDto user2) {
        return user1.id().longValue() == user2.id().longValue();
    }

}
