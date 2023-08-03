package io.github.daengdaenglee.wantedpreonboardingbackend.post.application.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByIdDesc(Pageable page);

    List<Post> findByIdLessThanOrderByIdDesc(Long id, Pageable page);

    Boolean existsByIdLessThan(Long id);

}
