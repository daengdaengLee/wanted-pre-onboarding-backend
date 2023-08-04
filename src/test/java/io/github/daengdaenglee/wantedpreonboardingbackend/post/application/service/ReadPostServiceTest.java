package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.ReadPostInboundPort;
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

class ReadPostServiceTest {

    private PostRepository mockPostRepository;

    private ReadPostService readPostService;

    @BeforeEach
    void beforeEach() {
        this.mockPostRepository = Mockito.mock(PostRepository.class);
        this.readPostService = new ReadPostService(this.mockPostRepository);
    }

    @Test
    @DisplayName("입력한 게시글 id 로 PostRepository 에서 게시글을 조회한다.")
    void read() {
        var postId = 1L;
        Mockito.when(this.mockPostRepository.findById(postId)).thenReturn(Optional.empty());

        this.readPostService.readPost(new ReadPostInboundPort.InputDto(postId));

        var passedPostIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(this.mockPostRepository).findById(passedPostIdCaptor.capture());
        var passedPostId = passedPostIdCaptor.getValue();

        assertThat(passedPostId.longValue()).isEqualTo(postId);
    }

    @Test
    @DisplayName("PostRepository 에서 게시글을 찾지 못하면 빈 Optional 객체를 반환한다.")
    void notExist() {
        Mockito.when(this.mockPostRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        var result = this.readPostService.readPost(new ReadPostInboundPort.InputDto(1L));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("PostRepository 에서 게시글을 찾으면 해당 게시글의 Optional 객체를 반환한다.")
    void exist() {
        var postId = 1L;
        var postTitle = "게시글 제목";
        var postContent = "게시글 내용";
        var authorId = 2L;
        var post = Mockito.mock(Post.class);
        Mockito.when(post.id()).thenReturn(postId);
        Mockito.when(post.title()).thenReturn(postTitle);
        Mockito.when(post.content()).thenReturn(postContent);
        Mockito.when(post.author()).thenReturn(new User(authorId));
        Mockito.when(this.mockPostRepository.findById(postId))
                .thenReturn(Optional.of(post));

        var result = this.readPostService.readPost(new ReadPostInboundPort.InputDto(postId));

        assertThat(result.isPresent()).isTrue();
        var resultPost = result.get();
        assertThat(resultPost.id()).isEqualTo(postId);
        assertThat(resultPost.title()).isEqualTo(postTitle);
        assertThat(resultPost.content()).isEqualTo(postContent);
        assertThat(resultPost.author().id()).isEqualTo(authorId);
    }

}