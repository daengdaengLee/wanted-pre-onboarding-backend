package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class AuthorizeServiceTest {

    private ReadPostInboundPort mockReadPostInboundPort;

    private AuthorizeService authorizeService;

    @BeforeEach
    void beforeEach() {
        this.mockReadPostInboundPort = Mockito.mock(ReadPostInboundPort.class);
        this.authorizeService = new AuthorizeService(this.mockReadPostInboundPort);
    }

    @Test
    @DisplayName("요청한 유저와 생성하려는 게시글의 작성자가 다르면 에러를 반환한다.")
    void canCreatePostDifferentUser() {
        var requestUserId = 1L;
        var authorId = 2L;
        var createPostDto = Mockito.mock(CreatePostDto.class);
        Mockito.when(createPostDto.author()).thenReturn(new UserDto(authorId));

        var result = this.authorizeService.canCreatePost(
                new AuthorizeInboundPort.CanCreatePostInputDto(
                        createPostDto,
                        new UserDto(requestUserId)));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(AuthorizeInboundPort.CanCreatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Test
    @DisplayName("요청한 유저와 생성하려는 게시글의 작성자가 같으면 true 를 반환한다.")
    void canCreatePostSameUser() {
        var userId = 1L;
        var createPostDto = Mockito.mock(CreatePostDto.class);
        Mockito.when(createPostDto.author()).thenReturn(new UserDto(userId));

        var result = this.authorizeService.canCreatePost(
                new AuthorizeInboundPort.CanCreatePostInputDto(
                        createPostDto,
                        new UserDto(userId)));

        assertThat(result.isRight()).isTrue();
        assertThat(result.right()).isTrue();
    }

    @Test
    @DisplayName("수정할 글을 PostRepository 에서 찾지 못하면 에러를 반환한다.")
    void canUpdatePostNotExistPost() {
        var postId = 1L;
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.empty();
                });

        var result = this.authorizeService.canUpdatePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(
                        postId,
                        new UserDto(2L)));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(AuthorizeInboundPort.CanUpdatePostErrorCode.NOT_EXIST);
    }

    @Test
    @DisplayName("수정하려는 게시글의 작성자와 요청한 유저가 다르면 에러를 반환한다.")
    void canUpdatePostDifferentUser() {
        var postId = 1L;
        var userId1 = 2L;
        var userId2 = 3L;
        var post = Mockito.mock(PostDto.class);
        Mockito.when(post.author()).thenReturn(new UserDto(userId1));
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.of(post);
                });

        var result = this.authorizeService.canUpdatePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, new UserDto(userId2)));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(AuthorizeInboundPort.CanUpdatePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Test
    @DisplayName("수정할 게시글의 작성자와 요청한 유저가 같으면 true 를 반환한다.")
    void canUpdatePostSameUser() {
        var postId = 1L;
        var userId = 2L;
        var post = Mockito.mock(PostDto.class);
        Mockito.when(post.author()).thenReturn(new UserDto(userId));
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.of(post);
                });

        var result = this.authorizeService.canUpdatePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, new UserDto(userId)));

        assertThat(result.isRight()).isTrue();
        assertThat(result.right()).isTrue();
    }

    @Test
    @DisplayName("삭제할 글을 PostRepository 에서 찾지 못하면 에러를 반환한다.")
    void canDeletePostNotExistPost() {
        var postId = 1L;
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.empty();
                });

        var result = this.authorizeService.canDeletePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(
                        postId,
                        new UserDto(2L)));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(AuthorizeInboundPort.CanDeletePostErrorCode.NOT_EXIST);
    }

    @Test
    @DisplayName("삭제하려는 게시글의 작성자와 요청한 유저가 다르면 에러를 반환한다.")
    void canDeletePostDifferentUser() {
        var postId = 1L;
        var userId1 = 2L;
        var userId2 = 3L;
        var post = Mockito.mock(PostDto.class);
        Mockito.when(post.author()).thenReturn(new UserDto(userId1));
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.of(post);
                });

        var result = this.authorizeService.canDeletePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, new UserDto(userId2)));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(AuthorizeInboundPort.CanDeletePostErrorCode.ILLEGAL_AUTHOR);
    }

    @Test
    @DisplayName("삭제할 게시글의 작성자와 요청한 유저가 같으면 true 를 반환한다.")
    void canDeletePostSameUser() {
        var postId = 1L;
        var userId = 2L;
        var post = Mockito.mock(PostDto.class);
        Mockito.when(post.author()).thenReturn(new UserDto(userId));
        Mockito.when(this.mockReadPostInboundPort.readPost(
                        Mockito.any(ReadPostInboundPort.InputDto.class)))
                .thenAnswer(invocation -> {
                    var inputDto = invocation.getArgument(0, ReadPostInboundPort.InputDto.class);
                    assertThat(inputDto.id()).isEqualTo(postId);
                    return Optional.of(post);
                });

        var result = this.authorizeService.canDeletePost(
                new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, new UserDto(userId)));

        assertThat(result.isRight()).isTrue();
        assertThat(result.right()).isTrue();
    }

}