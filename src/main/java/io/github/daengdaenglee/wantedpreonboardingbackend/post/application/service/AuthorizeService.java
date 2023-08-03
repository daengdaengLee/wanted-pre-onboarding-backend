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
    public Either<String, Boolean> canUpdatePost(CanUpdateOrDeletePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>("자신이 쓴 글만 수정할 수 있습니다.");
    }

    @Override
    public Either<String, Boolean> canDeletePost(CanUpdateOrDeletePostInputDto inputDto) {
        if (this.isSameUser(inputDto.post().author(), inputDto.requestUser())) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>("자신이 쓴 글만 지울 수 있습니다.");
    }

    private boolean isSameUser(UserDto user1, UserDto user2) {
        return user1.id().longValue() == user2.id().longValue();
    }

}
