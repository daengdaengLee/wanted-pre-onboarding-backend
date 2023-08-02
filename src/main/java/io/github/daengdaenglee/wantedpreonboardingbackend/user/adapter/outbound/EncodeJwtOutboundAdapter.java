package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.JwtConfig;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodeJwtOutboundPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class EncodeJwtOutboundAdapter implements EncodeJwtOutboundPort {

    private final String secret;

    private final Integer validityInSec;

    public EncodeJwtOutboundAdapter(JwtConfig jwtConfig) {
        this.secret = jwtConfig.secret();
        this.validityInSec = jwtConfig.validityInSec();
    }

    @Override
    public String encode(InputDto inputDto) {
        var calendar = Calendar.getInstance();
        calendar.setTime(inputDto.now());
        calendar.add(Calendar.SECOND, this.validityInSec);
        var expiration = calendar.getTime();
        return Jwts
                .builder()
                .setSubject(inputDto.userId().toString())
                .setIssuedAt(inputDto.now())
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(this.secret)))
                .compact();
    }

}
