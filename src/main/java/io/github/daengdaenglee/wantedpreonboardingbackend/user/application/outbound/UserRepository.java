package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
