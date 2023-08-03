package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.DeletePostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletePostService implements DeletePostInboundPort {

    private final PostRepository postRepository;

    public DeletePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void deletePost(InputDto inputDto) {
        this.postRepository.deleteById(inputDto.id());
    }

}
