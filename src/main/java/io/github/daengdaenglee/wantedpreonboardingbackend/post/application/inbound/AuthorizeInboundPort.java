package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public interface AuthorizeInboundPort {

    enum CanCreatePostErrorCode {

        ILLEGAL_AUTHOR("다른 사람으로 글을 쓸 수 없습니다.");

        private final String message;

        CanCreatePostErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    enum CanUpdatePostErrorCode {

        ILLEGAL_AUTHOR("자신이 쓴 글만 수정할 수 있습니다.");

        private final String message;

        CanUpdatePostErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    enum CanDeletePostErrorCode {

        ILLEGAL_AUTHOR("자신이 쓴 글만 삭제할 수 있습니다.");

        private final String message;

        CanDeletePostErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    record CanCreatePostInputDto(CreatePostDto post, UserDto requestUser) {
    }

    record CanUpdateOrDeletePostInputDto(PostDto post, UserDto requestUser) {
    }

    Either<CanCreatePostErrorCode, Boolean> canCreatePost(CanCreatePostInputDto inputDto);

    Either<CanUpdatePostErrorCode, Boolean> canUpdatePost(CanUpdateOrDeletePostInputDto inputDto);

    Either<CanDeletePostErrorCode, Boolean> canDeletePost(CanUpdateOrDeletePostInputDto inputDto);

}
