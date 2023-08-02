package io.github.daengdaenglee.wantedpreonboardingbackend.post.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.auth.Auth;
import io.github.daengdaenglee.wantedpreonboardingbackend.common.SimpleApiException;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.AuthorizeInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
public class PostController {

    private final AuthorizeInboundPort authorizeInboundPort;

    public PostController(
            AuthorizeInboundPort authorizeInboundPort
    ) {
        this.authorizeInboundPort = authorizeInboundPort;
    }

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
        var authResult = Auth.create(authentication);
        if (authResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        var auth = authResult.get();
        var requestUserDto = new UserDto(auth.userId());

        var authorDtoResult = UserDto.create(createPostInputDto.author().id());
        if (authorDtoResult.isLeft()) {
            var message = authorDtoResult.left();
            throw new SimpleApiException(HttpStatus.BAD_REQUEST, message);
        }
        var authorDto = authorDtoResult.right();
        var createPostDto = new CreatePostDto(
                createPostInputDto.title(),
                createPostInputDto.content(),
                authorDto);

        var canCreatePostInputDto = new AuthorizeInboundPort.CanCreatePostInputDto(createPostDto, requestUserDto);
        var canCreatePostResult = this.authorizeInboundPort.canCreatePost(canCreatePostInputDto);
        if (canCreatePostResult.isLeft()) {
            var message = canCreatePostResult.left();
            throw new SimpleApiException(HttpStatus.FORBIDDEN, message);
        }

        throw new RuntimeException("not implemented");
    }

}
