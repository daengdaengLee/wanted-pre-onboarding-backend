package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
