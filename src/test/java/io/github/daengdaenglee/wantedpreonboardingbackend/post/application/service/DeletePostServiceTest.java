package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.DeletePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class DeletePostServiceTest {

    private PostRepository mockPostRepository;

    private DeletePostService deletePostService;

    @BeforeEach
    void beforeEach() {
        this.mockPostRepository = Mockito.mock(PostRepository.class);
        this.deletePostService = new DeletePostService(this.mockPostRepository);
    }

    @Test
    @DisplayName("입력한 게시글 id 로 PostRepository 에 삭제 요청을 한다.")
    void success() {
        var postId = 1L;

        this.deletePostService.deletePost(new DeletePostInboundPort.InputDto(postId));

        var deletedPostIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(this.mockPostRepository).deleteById(deletedPostIdCaptor.capture());
        var deletedPostId = deletedPostIdCaptor.getValue();

        assertThat(deletedPostId.longValue()).isEqualTo(postId);
    }

}