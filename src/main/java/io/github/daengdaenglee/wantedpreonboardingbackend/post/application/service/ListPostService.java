package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.ListPostInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.PostDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.inbound.UserDto;
import io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListPostService implements ListPostInboundPort {

    private final PostRepository postRepository;

    public ListPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public OutputDto listPost(InputDto inputDto) {
        var page = PageRequest.of(0, inputDto.count());
        var posts = inputDto
                .cursor()
                .map(cursor -> this.postRepository.findByIdLessThanOrderByIdDesc(cursor, page))
                .orElseGet(() -> this.postRepository.findAllByOrderByIdDesc(page))
                .stream()
                .map(post -> new PostDto(
                        post.id(),
                        post.title(),
                        post.content(),
                        new UserDto(post.author().id())))
                .toList();

        Optional<PostDto> lastPost = posts.isEmpty()
                ? Optional.empty()
                : Optional.of(posts.get(posts.size() - 1));
        var hasNext = lastPost
                .map(PostDto::id)
                .map(this.postRepository::existsByIdLessThan)
                .orElse(false);

        return new OutputDto(posts, hasNext);
    }

}
