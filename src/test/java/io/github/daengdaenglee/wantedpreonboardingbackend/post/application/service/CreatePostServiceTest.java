package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePostServiceTest {

    private PostRepository mockPostRepository;

    private CreatePostService createPostService;

    @BeforeEach
    void beforeEach() {
        this.mockPostRepository = Mockito.mock(PostRepository.class);
        this.createPostService = new CreatePostService(this.mockPostRepository);
    }

    @Test
    @DisplayName("입력한 title, content, author id 대로 저장한다.")
    void save() {
        var title = "게시글 재목";
        var content = "게시글 내용";
        var authorId = 1L;
        var post = Mockito.mock(Post.class);
        Mockito.when(post.id()).thenReturn(1L);
        Mockito.when(post.title()).thenReturn(title);
        Mockito.when(post.content()).thenReturn(content);
        Mockito.when(post.author()).thenReturn(new User(authorId));
        Mockito.when(this.mockPostRepository.save(Mockito.any(Post.class))).thenReturn(post);

        this.createPostService.createPost(new CreatePostDto(title, content, new UserDto(authorId)));

        var savedPostCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(this.mockPostRepository).save(savedPostCaptor.capture());
        var savedPost = savedPostCaptor.getValue();

        assertThat(savedPost.title()).isEqualTo(title);
        assertThat(savedPost.content()).isEqualTo(content);
        assertThat(savedPost.author().id()).isEqualTo(authorId);
    }

    @Test
    @DisplayName("입력한 title, content, author id 에 맞게 저장한 Post 를 반환한다.")
    void success() {
        var postId = 1L;
        var title = "게시글 재목";
        var content = "게시글 내용";
        var authorId = 2L;
        var post = Mockito.mock(Post.class);
        Mockito.when(post.id()).thenReturn(postId);
        Mockito.when(post.title()).thenReturn(title);
        Mockito.when(post.content()).thenReturn(content);
        Mockito.when(post.author()).thenReturn(new User(authorId));
        Mockito.when(this.mockPostRepository.save(Mockito.any(Post.class)))
                .thenAnswer(invocation -> {
                    var inputPost = invocation.getArgument(0, Post.class);
                    assertThat(inputPost.title()).isEqualTo(title);
                    assertThat(inputPost.content()).isEqualTo(content);
                    assertThat(inputPost.author().id()).isEqualTo(authorId);
                    return post;
                });

        var result = this.createPostService.createPost(
                new CreatePostDto(title, content, new UserDto(authorId)));

        assertThat(result.id()).isEqualTo(postId);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.author().id()).isEqualTo(authorId);
    }

}