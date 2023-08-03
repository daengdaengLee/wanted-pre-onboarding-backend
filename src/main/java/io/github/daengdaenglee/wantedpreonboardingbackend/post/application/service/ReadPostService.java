package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.ReadPostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReadPostService implements ReadPostInboundPort {

    private final PostRepository postRepository;

    public ReadPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Optional<PostDto> readPost(InputDto inputDto) {
        return this.postRepository
                .findById(inputDto.id())
                .map(post -> new PostDto(
                        post.id(),
                        post.title(),
                        post.content(),
                        new UserDto(post.author().id())));
    }

}
