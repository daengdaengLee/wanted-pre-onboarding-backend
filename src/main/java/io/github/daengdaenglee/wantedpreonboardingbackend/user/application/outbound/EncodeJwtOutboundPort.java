package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound;

import java.util.Date;

public interface EncodeJwtOutboundPort {

    record InputDto(Long userId, Date now) {
    }

    String encode(InputDto inputDto);

}
