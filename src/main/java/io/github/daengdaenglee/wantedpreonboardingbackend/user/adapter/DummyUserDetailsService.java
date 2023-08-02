package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DummyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new RuntimeException("스프링 시큐리티 기본 설정을 덮어쓰기 위한 더미 객체입니다. 사용하지 마세요.");
    }
}
