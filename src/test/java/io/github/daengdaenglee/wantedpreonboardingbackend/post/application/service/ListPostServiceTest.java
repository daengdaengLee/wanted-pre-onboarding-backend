package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.ListPostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

class ListPostServiceTest {

    private PostRepository mockPostRepository;

    private ListPostService listPostService;

    private List<Post> mockPosts;

    private List<PostDto> matchedPostDtos;

    @BeforeEach
    void beforeEach() {
        this.mockPostRepository = Mockito.mock(PostRepository.class);
        this.listPostService = new ListPostService(this.mockPostRepository);
        this.mockPosts = LongStream
                .range(0, 10)
                .map(i -> 10 - i)
                .mapToObj(postId -> {
                    var title = "게시글 제목 " + postId;
                    var content = "게시글 내용 " + postId;
                    var userId = postId + 20;
                    var mockPost = Mockito.mock(Post.class);
                    Mockito.when(mockPost.id()).thenReturn(postId);
                    Mockito.when(mockPost.title()).thenReturn(title);
                    Mockito.when(mockPost.content()).thenReturn(content);
                    Mockito.when(mockPost.author()).thenReturn(new User(userId));
                    return mockPost;
                })
                .toList();
        this.matchedPostDtos = this.mockPosts
                .stream()
                .map(post -> new PostDto(
                        post.id(),
                        post.title(),
                        post.content(),
                        new UserDto(post.author().id())))
                .toList();
    }

    @Test
    @DisplayName("cursor 를 입력한 경우 cursor + count 로 게시글을 조회한다.")
    void cursorExist() {
        var cursor = 10L;
        var count = 5;
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        var cursorCaptor = ArgumentCaptor.forClass(Long.class);
        var pageCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(this.mockPostRepository)
                .findByIdLessThanOrderByIdDesc(cursorCaptor.capture(), pageCaptor.capture());
        var passedCursor = cursorCaptor.getValue();
        var passedPage = pageCaptor.getValue();

        assertThat(passedCursor.longValue()).isEqualTo(cursor);
        assertThat(passedPage.getPageSize()).isEqualTo(count);
    }

    @Test
    @DisplayName("cursor 를 입력하지 않은 경우 count 로 게시글을 조회한다.")
    void cursorNotExist() {
        var count = 5;
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(new ArrayList<>());

        this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        var pageCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(this.mockPostRepository)
                .findAllByOrderByIdDesc(pageCaptor.capture());
        var passedPage = pageCaptor.getValue();

        assertThat(passedPage.getPageSize()).isEqualTo(count);
    }

    @Test
    @DisplayName("cursor + count 로 조회한 게시글 목록을 반환한다.")
    void postList1() {
        var cursor = this.mockPosts.get(0).id() + 1;
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.mockPosts);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        assertThat(result.posts()).isEqualTo(this.matchedPostDtos);
    }

    @Test
    @DisplayName("count 로 조회한 게시글 목록을 반환한다.")
    void postList2() {
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(this.mockPosts);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        assertThat(result.posts()).isEqualTo(this.matchedPostDtos);
    }

    @Test
    @DisplayName("cursor + count 로 조회한 게시글이 없는 경우 hasNext 가 false 이다.")
    void hasNextWhenEmptyPostList1() {
        var cursor = 10L;
        var count = 5;
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("count 로 조회한 게시글이 없는 경우 hasNext 가 false 이다.")
    void hasNextWhenEmptyPostList2() {
        var count = 5;
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(new ArrayList<>());

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("cursor + count 로 조회한 게시글이 있는 경우 마지막 게시글 다음 게시글이 있는지 조회한다.")
    void existNextPost1() {
        var cursor = this.mockPosts.get(0).id() + 1;
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.mockPosts);

        this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        var postIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(this.mockPostRepository).existsByIdLessThan(postIdCaptor.capture());
        var postId = postIdCaptor.getValue();

        var lastMockPost = this.mockPosts.get(this.mockPosts.size() - 1);
        assertThat(postId.longValue()).isEqualTo(lastMockPost.id().longValue());
    }

    @Test
    @DisplayName("count 로 조회한 게시글이 있는 경우 마지막 게시글 다음 게시글이 있는지 조회한다.")
    void existNextPost2() {
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(this.mockPosts);

        this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        var postIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(this.mockPostRepository).existsByIdLessThan(postIdCaptor.capture());
        var postId = postIdCaptor.getValue();

        var lastMockPost = this.mockPosts.get(this.mockPosts.size() - 1);
        assertThat(postId.longValue()).isEqualTo(lastMockPost.id().longValue());
    }

    @Test
    @DisplayName("cursor + count 로 조회한 마지막 게시글 다음 게시글이 없으면 hasNext 는 false 이다.")
    void hasNextWhenNotExistNextPost1() {
        var cursor = this.mockPosts.get(0).id() + 1;
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.mockPosts);
        Mockito.when(this.mockPostRepository.existsByIdLessThan(Mockito.anyLong()))
                .thenReturn(false);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("count 로 조회한 마지막 게시글 다음 게시글이 없으면 hasNext 는 false 이다.")
    void hasNextWhenNotExistNextPost2() {
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(this.mockPosts);
        Mockito.when(this.mockPostRepository.existsByIdLessThan(Mockito.anyLong()))
                .thenReturn(false);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("cursor + count 로 조회한 마지막 게시글 다음 게시글이 있으면 hasNext 는 true 이다.")
    void hasNextWhenExistNextPost1() {
        var cursor = this.mockPosts.get(0).id() + 1;
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findByIdLessThanOrderByIdDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.mockPosts);
        Mockito.when(this.mockPostRepository.existsByIdLessThan(Mockito.anyLong()))
                .thenReturn(true);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.of(cursor), count));

        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("count 로 조회한 마지막 게시글 다음 게시글이 있으면 hasNext 는 true 이다.")
    void hasNextWhenExistNextPost2() {
        var count = this.mockPosts.size();
        Mockito.when(this.mockPostRepository.findAllByOrderByIdDesc(Mockito.any()))
                .thenReturn(this.mockPosts);
        Mockito.when(this.mockPostRepository.existsByIdLessThan(Mockito.anyLong()))
                .thenReturn(true);

        var result = this.listPostService.listPost(
                new ListPostInboundPort.InputDto(Optional.empty(), count));

        assertThat(result.hasNext()).isTrue();
    }

}