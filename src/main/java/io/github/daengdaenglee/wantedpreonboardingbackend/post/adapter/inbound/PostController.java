package io.github.daengdaenglee.wantedpreonboardingbackend.post.adapter.inbound;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
public class PostController {

    public record AuthorDto(String id) {
    }

    public record CreatePostInputDto(String title, String content, AuthorDto author) {
    }

    public record PostOutputDto(
            String id,
            String title,
            String content,
            AuthorDto author) {
    }

    public record CreatePostOutputDto(PostOutputDto post) {
    }

    @PostMapping()
    public CreatePostOutputDto createPost(
            @RequestBody CreatePostInputDto createPostInputDto,
            Authentication authentication) {
        throw new RuntimeException("not implemented");
    }

}
