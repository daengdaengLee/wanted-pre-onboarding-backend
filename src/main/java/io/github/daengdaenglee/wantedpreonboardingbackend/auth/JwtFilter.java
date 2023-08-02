package io.github.daengdaenglee.wantedpreonboardingbackend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String header;

    private final String prefix;

    private final String secret;

    public JwtFilter(JwtConfig jwtConfig) {
        this.header = jwtConfig.header();
        this.prefix = jwtConfig.prefix();
        this.secret = jwtConfig.secret();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        this.getToken(request).flatMap(this::decodeJwt).ifPresent(userId -> {
            var authentication = UsernamePasswordAuthenticationToken.authenticated(userId, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            return Optional.empty();
        }
        var header = ((HttpServletRequest) request).getHeader(this.header);
        if (!StringUtils.hasText(header) || !header.startsWith(this.prefix)) {
            return Optional.empty();
        }
        var token = header.substring(this.prefix.length());
        return Optional.of(token);
    }

    private Optional<Long> decodeJwt(String token) {
        try {
            var key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(this.secret));
            var userId = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return Optional.of(Long.valueOf(userId));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
