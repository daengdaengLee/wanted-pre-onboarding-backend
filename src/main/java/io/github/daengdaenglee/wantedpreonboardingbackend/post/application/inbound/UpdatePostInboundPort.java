package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public interface UpdatePostInboundPort {

    enum ErrorCode {

        NOT_EXIST("해당 게시글이 없습니다.");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    record UpdatePostDto(Long id, String title, String content) {
    }

    Either<ErrorCode, PostDto> updatePost(UpdatePostDto post);

}
