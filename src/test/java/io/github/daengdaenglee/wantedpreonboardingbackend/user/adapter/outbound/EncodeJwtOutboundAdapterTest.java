package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.auth.JwtConfig;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodeJwtOutboundPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class EncodeJwtOutboundAdapterTest {

    @Test
    @DisplayName("설정값에 따라 JWT 를 생성한다.")
    void encodeJwt() {
        var userId = 1L;
        var now = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(now);
        var millisecond = calendar.get(Calendar.MILLISECOND);
        calendar.add(Calendar.MILLISECOND, -millisecond);
        now = calendar.getTime();
        var secret = Encoders.BASE64URL.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        var validityInSec = 24 * 60 * 60;
        var jwtConfig = Mockito.mock(JwtConfig.class);
        Mockito.when(jwtConfig.secret()).thenReturn(secret);
        Mockito.when(jwtConfig.validityInSec()).thenReturn(validityInSec);
        var encodeJwtOutboundAdapter = new EncodeJwtOutboundAdapter(jwtConfig);

        var result = encodeJwtOutboundAdapter.encode(
                new EncodeJwtOutboundPort.InputDto(userId, now));
        var parsed = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret)))
                .build()
                .parseClaimsJws(result)
                .getBody();

        assertThat(parsed.getSubject()).isEqualTo(String.valueOf(userId));
        assertThat(parsed.getIssuedAt()).isEqualTo(now);
        assertThat(parsed.getExpiration().getTime()).isEqualTo(now.getTime() + (validityInSec * 1000));
    }

}