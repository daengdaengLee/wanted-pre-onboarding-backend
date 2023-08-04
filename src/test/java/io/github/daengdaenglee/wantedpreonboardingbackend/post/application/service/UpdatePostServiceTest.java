package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UpdatePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatePostServiceTest {

    private PostRepository mockPostRepository;

    private UpdatePostService updatePostService;

    @BeforeEach
    void beforeEach() {
        this.mockPostRepository = Mockito.mock(PostRepository.class);
        this.updatePostService = new UpdatePostService(this.mockPostRepository);
    }

    @Test
    @DisplayName("입력한 게시글 id 로 PostRepository 에서 게시글을 찾는다.")
    void read() {
        var postId = 1L;
        Mockito.when(this.mockPostRepository.findById(postId)).thenReturn(Optional.empty());

        this.updatePostService.updatePost(
                new UpdatePostInboundPort.UpdatePostDto(postId, "바꿀 제목", "바꿀 내용"));

        var passedPostIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(this.mockPostRepository).findById(passedPostIdCaptor.capture());
        var passedPostId = passedPostIdCaptor.getValue();

        assertThat(passedPostId).isEqualTo(postId);
    }

    @Test
    @DisplayName("게시글이 없으면 에러를 반환한다.")
    void notExist() {
        var postId = 1L;
        Mockito.when(this.mockPostRepository.findById(postId)).thenReturn(Optional.empty());

        var result = this.updatePostService.updatePost(
                new UpdatePostInboundPort.UpdatePostDto(postId, "바꿀 제목", "바꿀 내용"));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(UpdatePostInboundPort.ErrorCode.NOT_EXIST);
    }

    @Test
    @DisplayName("입력한 title, content 로 게시글을 바꿔서 저장한다.")
    void update() {
        var postId = 1L;
        var postTitle = "바꿀 제목";
        var postContent = "바꿀 내용";
        var originalPost = Mockito.mock(Post.class);
        var updatedPost = Mockito.mock(Post.class);
        Mockito.when(this.mockPostRepository.findById(postId))
                .thenReturn(Optional.of(originalPost));
        Mockito.when(this.mockPostRepository.save(Mockito.any()))
                .thenReturn(updatedPost);
        Mockito.when(updatedPost.id()).thenReturn(postId);
        Mockito.when(updatedPost.title()).thenReturn(postTitle);
        Mockito.when(updatedPost.content()).thenReturn(postContent);
        Mockito.when(updatedPost.author()).thenReturn(new User(2L));

        this.updatePostService.updatePost(
                new UpdatePostInboundPort.UpdatePostDto(postId, postTitle, postContent));

        var passedPostCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(this.mockPostRepository).save(passedPostCaptor.capture());
        var passedPost = passedPostCaptor.getValue();

        var passedTitleCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(originalPost).setTitle(passedTitleCaptor.capture());
        var passedTitle = passedTitleCaptor.getValue();

        var passedContentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(originalPost).setContent(passedContentCaptor.capture());
        var passedContent = passedContentCaptor.getValue();

        assertThat(passedPost).isEqualTo(originalPost);
        assertThat(passedTitle).isEqualTo(postTitle);
        assertThat(passedContent).isEqualTo(postContent);
    }

    @Test
    @DisplayName("바꾼 게시물을 반환한다..")
    void success() {
        var postId = 1L;
        var postTitle = "바꿀 제목";
        var postContent = "바꿀 내용";
        var authorId = 2L;
        var originalPost = Mockito.mock(Post.class);
        var updatedPost = Mockito.mock(Post.class);
        Mockito.when(this.mockPostRepository.findById(postId))
                .thenReturn(Optional.of(originalPost));
        Mockito.when(this.mockPostRepository.save(Mockito.any()))
                .thenReturn(updatedPost);
        Mockito.when(updatedPost.id()).thenReturn(postId);
        Mockito.when(updatedPost.title()).thenReturn(postTitle);
        Mockito.when(updatedPost.content()).thenReturn(postContent);
        Mockito.when(updatedPost.author()).thenReturn(new User(authorId));

        var result = this.updatePostService.updatePost(
                new UpdatePostInboundPort.UpdatePostDto(postId, "바꿀 제목", "바꿀 내용"));

        assertThat(result.isRight()).isTrue();
        var resultPost = result.right();
        assertThat(resultPost.id()).isEqualTo(postId);
        assertThat(resultPost.title()).isEqualTo(postTitle);
        assertThat(resultPost.content()).isEqualTo(postContent);
        assertThat(resultPost.author().id()).isEqualTo(authorId);
    }

}