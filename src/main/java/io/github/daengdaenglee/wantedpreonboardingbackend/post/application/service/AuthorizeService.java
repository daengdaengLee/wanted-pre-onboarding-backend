package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.AuthorizeInboundPort;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements AuthorizeInboundPort {

    @Override
    public Either<String, Boolean> canCreatePost(CanCreatePostInputDto inputDto) {
        if (inputDto.post().author().id().longValue() == inputDto.requestUser().id().longValue()) {
            return new Either.Right<>(true);
        }
        return new Either.Left<>("다른 사람으로 글을 쓸 수 없습니다.");
    }

}
