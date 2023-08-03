package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.ReadUserInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class ReadUserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private ReadUserService readUserService;

    @BeforeEach
    void beforeEach() {
        this.readUserService = new ReadUserService(this.mockUserRepository);
    }

    @Test
    @DisplayName("UserRepository 에서 id 에 맞는 User 를 찾지 못하면 빈 Optional 객체를 반환한다.")
    void userNotExist() {
        var userId = 1L;
        Mockito.when(this.mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        var result = this.readUserService.readUser(new ReadUserInboundPort.InputDto(userId));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("""
            UserRepository 에서 id 에 맞는 User 를 찾으면
            해당 User 의 id, email 을 담고있는 Dto 객체를 Optional 형태로 반환한다.
            """)
    void userExist() {
        var userId = 1L;
        var userEmail = "example@email.com";
        var user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(userId);
        Mockito.when(user.email()).thenReturn(userEmail);
        Mockito.when(this.mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        var result = this.readUserService.readUser(new ReadUserInboundPort.InputDto(userId));

        assertThat(result.isPresent()).isTrue();
        var dto = result.get();
        assertThat(dto.id()).isEqualTo(userId);
        assertThat(dto.email()).isEqualTo(userEmail);
    }

}