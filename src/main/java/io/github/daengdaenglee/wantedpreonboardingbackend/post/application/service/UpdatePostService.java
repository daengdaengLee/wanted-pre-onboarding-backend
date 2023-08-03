package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UpdatePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdatePostService implements UpdatePostInboundPort {

    private final PostRepository postRepository;

    public UpdatePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Either<ErrorCode, PostDto> updatePost(UpdatePostDto post) {
        var currentPostResult = this.postRepository.findById(post.id());
        if (currentPostResult.isEmpty()) {
            return new Either.Left<>(ErrorCode.NOT_EXIST);
        }
        var currentPost = currentPostResult.get();
        currentPost.setTitle(post.title());
        currentPost.setContent(post.content());
        var updatedPost = this.postRepository.save(currentPost);
        return new Either.Right<>(new PostDto(
                updatedPost.id(),
                updatedPost.title(),
                updatedPost.content(),
                new UserDto(updatedPost.author().id())));
    }

}
