package io.github.daengdaenglee.wantedpreonboardingbackend.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    @DisplayName("""
            게시글 제목, 게시글 내용, 게시글 작성자 아이디로 Post 객체를 만들면
            title() getter, content() getter, author() getter 에서
            그대로 반환한다.
            """)
    void createByTitleAndContentAndAuthorId() {
        var inputTitle = "게시글 제목";
        var inputContent = "게시글 내용";
        var inputAuthorId = 1L;

        var post = new Post(inputTitle, inputContent, inputAuthorId);

        assertThat(post.title()).isEqualTo(inputTitle);
        assertThat(post.content()).isEqualTo(inputContent);
        assertThat(post.author().id()).isEqualTo(inputAuthorId);
    }

    @Test
    @DisplayName("setTitle() setter 로 게시글 제목을 변경할 수 있다.")
    void setTitle() {
        var updatedTitle = "바꾼 제목";
        var post = new Post("원래 제목", "게시글 내용", 1L);

        post.setTitle(updatedTitle);

        assertThat(post.title()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("setContent() setter 로 게시글 내용을 변경할 수 있다.")
    void setContent() {
        var updatedContent = "바꾼 내용";
        var post = new Post("게시글 제목", "원래 내용", 1L);

        post.setContent(updatedContent);

        assertThat(post.content()).isEqualTo(updatedContent);
    }

}