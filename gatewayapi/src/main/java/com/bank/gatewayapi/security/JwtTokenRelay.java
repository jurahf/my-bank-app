package com.bank.gatewayapi.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public abstract class JwtTokenRelay {

    private static final Logger log =
            LoggerFactory.getLogger(JwtTokenRelay.class);

    private JwtTokenRelay() {
    }

    /**
     * MVC-фильтр Token Relay: забирает JWT из входящего заголовка Authorization
     * и пробрасывает его в исходящий запрос к микросервису.
     *
     * В application.yml используется как:
     * <pre>
     * filters:
     *   - name: JwtTokenRelay
     * </pre>
     */
    @Shortcut
    public static HandlerFilterFunction<ServerResponse, ServerResponse> jwtTokenRelay() {
        return (request, next) -> {
            String authorization = request.headers().firstHeader("Authorization");

            if (authorization == null || authorization.isBlank()) {
                log.warn("No Authorization header found for path {}",
                        request.uri().getPath());
                return next.handle(request);
            }

            ServerRequest mutated = ServerRequest.from(request)
                    .headers(headers -> headers.set("Authorization", authorization))
                    .build();

            log.debug("Token relayed for path {} (len={})",
                    request.uri().getPath(), authorization.length());

            return next.handle(mutated);
        };
    }

    public static class FilterSupplier extends SimpleFilterSupplier {

        public FilterSupplier() {
            super(JwtTokenRelay.class);
        }
    }
}
