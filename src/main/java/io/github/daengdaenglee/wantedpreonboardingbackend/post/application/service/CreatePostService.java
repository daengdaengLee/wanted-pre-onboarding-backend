package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.CreatePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import org.springframework.stereotype.Service;

@Service
public class CreatePostService implements CreatePostInboundPort {

    @Override
    public PostDto createPost(CreatePostDto post) {
        throw new RuntimeException("not implemented");
    }

}
