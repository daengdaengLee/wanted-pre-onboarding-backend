package io.github.daengdaenglee.wantedpreonboardingbackend.post.adapter.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.auth.Auth;
import io.github.daengdaenglee.wantedpreonboardingbackend.common.SimpleApiException;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final AuthorizeInboundPort authorizeInboundPort;

    private final CreatePostInboundPort createPostInboundPort;

    private final ReadPostInboundPort readPostInboundPort;

    private final UpdatePostInboundPort updatePostInboundPort;

    private final DeletePostInboundPort deletePostInboundPort;

    private final ListPostInboundPort listPostInboundPort;

    public PostController(
            AuthorizeInboundPort authorizeInboundPort,
            CreatePostInboundPort createPostInboundPort,
            ReadPostInboundPort readPostInboundPort,
            UpdatePostInboundPort updatePostInboundPort,
            DeletePostInboundPort deletePostInboundPort,
            ListPostInboundPort listPostInboundPort) {
        this.authorizeInboundPort = authorizeInboundPort;
        this.createPostInboundPort = createPostInboundPort;
        this.readPostInboundPort = readPostInboundPort;
        this.updatePostInboundPort = updatePostInboundPort;
        this.deletePostInboundPort = deletePostInboundPort;
        this.listPostInboundPort = listPostInboundPort;
    }

    public record AuthorDto(String id) {

        public void validate() {
            if (this.id() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "작성자 아이디가 없습니다.");
            }
        }

    }

    public record CreatePostInputDto(String title, String content, AuthorDto author) {

        public void validate() {
            if (this.title() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "게시글 제목이 없습니다.");
            }
            if (this.content() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "게시글 내용이 없습니다.");
            }
            var author = this.author();
            if (author == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "게시글 작성자가 없습니다.");
            }
            author.validate();
        }

    }

    public record UpdatePostInputDto(String title, String content) {

        public void validate() {
            if (this.title() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "게시글 제목이 없습니다.");
            }
            if (this.content() == null) {
                throw new SimpleApiException(HttpStatus.BAD_REQUEST, "게시글 내용이 없습니다.");
            }
        }

    }

    public record PostOutputDto(
            String id,
            String title,
            String content,
            AuthorDto author) {
    }

    public record SinglePostOutputDto(PostOutputDto post) {
    }

    public record MultiplePostOutputDto(List<PostOutputDto> posts, Boolean hasNext) {
    }

    public record DeletePostOutputDto(String message) {
        DeletePostOutputDto() {
            this("게시글을 삭제했습니다.");
        }
    }

    @PostMapping()
    public SinglePostOutputDto createPost(
            @RequestBody CreatePostInputDto createPostInputDto,
            Authentication authentication) {
        createPostInputDto.validate();

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
            var errorCode = canCreatePostResult.left();
            if (errorCode == AuthorizeInboundPort.CanCreatePostErrorCode.ILLEGAL_AUTHOR) {
                throw new SimpleApiException(HttpStatus.FORBIDDEN, errorCode.message());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
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
        updatePostInputDto.validate();

        var authResult = Auth.create(authentication);
        if (authResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        var auth = authResult.get();
        var requestUserDto = new UserDto(auth.userId());

        var canUpdatePostInputDto = new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, requestUserDto);
        var canUpdatePostResult = this.authorizeInboundPort.canUpdatePost(canUpdatePostInputDto);
        if (canUpdatePostResult.isLeft()) {
            var errorCode = canUpdatePostResult.left();
            if (errorCode == AuthorizeInboundPort.CanUpdatePostErrorCode.ILLEGAL_AUTHOR) {
                throw new SimpleApiException(HttpStatus.FORBIDDEN, errorCode.message());
            } else if (errorCode == AuthorizeInboundPort.CanUpdatePostErrorCode.NOT_EXIST) {
                throw new SimpleApiException(HttpStatus.NOT_FOUND, errorCode.message());
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
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
    public DeletePostOutputDto deletePost(
            @PathVariable("postId") Long postId,
            Authentication authentication) {
        var authResult = Auth.create(authentication);
        if (authResult.isEmpty()) {
            throw new SimpleApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        var auth = authResult.get();
        var requestUserDto = new UserDto(auth.userId());

        var canDeletePostInputDto = new AuthorizeInboundPort.CanUpdateOrDeletePostInputDto(postId, requestUserDto);
        var canDeletePostResult = this.authorizeInboundPort.canDeletePost(canDeletePostInputDto);
        if (canDeletePostResult.isLeft()) {
            var errorCode = canDeletePostResult.left();
            if (errorCode == AuthorizeInboundPort.CanDeletePostErrorCode.ILLEGAL_AUTHOR) {
                throw new SimpleApiException(HttpStatus.FORBIDDEN, errorCode.message());
            } else if (errorCode == AuthorizeInboundPort.CanDeletePostErrorCode.NOT_EXIST) {
                return new DeletePostOutputDto();
            }
            this.logger.warn("{} 에러 코드 처리가 없습니다.", errorCode);
            throw new SimpleApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message());
        }

        this.deletePostInboundPort.deletePost(new DeletePostInboundPort.InputDto(postId));

        return new DeletePostOutputDto();
    }

    @GetMapping()
    public MultiplePostOutputDto listPost(
            @RequestParam(required = false, value = "cursor") Long cursor,
            @RequestParam(required = false, value = "count", defaultValue = "10") Integer count) {
        var listPostResult = this.listPostInboundPort.listPost(
                new ListPostInboundPort.InputDto(Optional.ofNullable(cursor), count));
        var posts = listPostResult
                .posts()
                .stream()
                .map(post -> new PostOutputDto(
                        post.id().toString(),
                        post.title(),
                        post.content(),
                        new AuthorDto(post.author().id().toString())))
                .toList();
        return new MultiplePostOutputDto(posts, listPostResult.hasNext());
    }

}
