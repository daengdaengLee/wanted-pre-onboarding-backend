package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.stereotype.Service;

@Service
public class CreatePostService implements CreatePostInboundPort {

    private final PostRepository postRepository;

    public CreatePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto) {
        var post = this.postRepository.save(new Post(
                createPostDto.title(),
                createPostDto.content(),
                createPostDto.author().id()));
        return new PostDto(
                post.id(),
                post.title(),
                post.content(),
                new UserDto(post.author().id()));
    }

}
