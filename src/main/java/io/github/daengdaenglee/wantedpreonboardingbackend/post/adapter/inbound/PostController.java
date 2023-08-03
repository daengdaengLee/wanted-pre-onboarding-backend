package io.github.daengdaenglee.wantedpreonboardingbackend.post.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.auth.Auth;
import io.github.daengdaenglee.wantedpreonboardingbackend.common.SimpleApiException;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {

    private final AuthorizeInboundPort authorizeInboundPort;

    private final CreatePostInboundPort createPostInboundPort;

    private final ReadPostInboundPort readPostInboundPort;

    public PostController(
            AuthorizeInboundPort authorizeInboundPort,
            CreatePostInboundPort createPostInboundPort,
            ReadPostInboundPort readPostInboundPort) {
        this.authorizeInboundPort = authorizeInboundPort;
        this.createPostInboundPort = createPostInboundPort;
        this.readPostInboundPort = readPostInboundPort;
    }

    public record AuthorDto(String id) {
    }

    public record CreatePostInputDto(String title, String content, AuthorDto author) {
    }

    public record UpdatePostInputDto(String title, String content) {
    }

    public record PostOutputDto(
            String id,
            String title,
            String content,
            AuthorDto author) {
    }

    public record SinglePostOutputDto(PostOutputDto post) {
    }

    @PostMapping()
    public SinglePostOutputDto createPost(
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

        var post = this.createPostInboundPort.createPost(createPostDto);
        return new SinglePostOutputDto(new PostOutputDto(
                post.id().toString(),
                post.title(),
                post.content(),
                new AuthorDto(post.author().id().toString())));
    }

    @GetMapping("{postId}")
    public SinglePostOutputDto readPost(@PathVariable("postId") Long postId) {
        var postResult = this.readPostInboundPort.readPost(
                new ReadPostInboundPort.InputDto(postId));
        if (postResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
        }
        var post = postResult.get();
        return new SinglePostOutputDto(new PostOutputDto(
                post.id().toString(),
                post.title(),
                post.content(),
                new AuthorDto(post.author().id().toString())));
    }

    @PutMapping("{postId}")
    public SinglePostOutputDto updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody UpdatePostInputDto updatePostInputDto,
            Authentication authentication) {
        var authResult = Auth.create(authentication);
        if (authResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        throw new RuntimeException("not implemented");
    }

}
