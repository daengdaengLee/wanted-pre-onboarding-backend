package io.github.daengdaenglee.wantedpreonboardingbackend.post.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.auth.Auth;
import io.github.daengdaenglee.wantedpreonboardingbackend.common.SimpleApiException;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final AuthorizeInboundPort authorizeInboundPort;

    private final CreatePostInboundPort createPostInboundPort;

    private final ReadPostInboundPort readPostInboundPort;

    private final UpdatePostInboundPort updatePostInboundPort;

    public PostController(
            AuthorizeInboundPort authorizeInboundPort,
            CreatePostInboundPort createPostInboundPort,
            ReadPostInboundPort readPostInboundPort,
            UpdatePostInboundPort updatePostInboundPort) {
        this.authorizeInboundPort = authorizeInboundPort;
        this.createPostInboundPort = createPostInboundPort;
        this.readPostInboundPort = readPostInboundPort;
        this.updatePostInboundPort = updatePostInboundPort;
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
        var auth = authResult.get();
        var requestUserDto = new UserDto(auth.userId());

        var currentPostResult = this.readPostInboundPort.readPost(
                new ReadPostInboundPort.InputDto(postId));
        if (currentPostResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
        }
        var currentPost = currentPostResult.get();

        var canUpdatePostInputDto = new AuthorizeInboundPort.CanUpdatePostInputDto(currentPost, requestUserDto);
        var canUpdatePostResult = this.authorizeInboundPort.canUpdatePost(canUpdatePostInputDto);
        if (canUpdatePostResult.isLeft()) {
            var message = canUpdatePostResult.left();
            throw new SimpleApiException(HttpStatus.FORBIDDEN, message);
        }

        var updatedPostResult = this.updatePostInboundPort.updatePost(
                new UpdatePostInboundPort.UpdatePostDto(
                        postId,
                        updatePostInputDto.title(),
                        updatePostInputDto.content()));
        if (updatedPostResult.isLeft()) {
            var errorCode = updatedPostResult.left();
            if (errorCode == UpdatePostInboundPort.ErrorCode.NOT_EXIST) {
                throw new SimpleApiException(HttpStatus.NOT_FOUND, errorCode.message());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
        }
        var updatedPost = updatedPostResult.right();

        return new SinglePostOutputDto(new PostOutputDto(
                updatedPost.id().toString(),
                updatedPost.title(),
                updatedPost.content(),
                new AuthorDto(updatedPost.author().id().toString())));
    }

    @DeleteMapping("{postId}")
    public void deletePost(
            @PathVariable("postId") Long postId,
            Authentication authentication) {
        var authResult = Auth.create(authentication);
        if (authResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        throw new RuntimeException("not implemented");
    }

}
