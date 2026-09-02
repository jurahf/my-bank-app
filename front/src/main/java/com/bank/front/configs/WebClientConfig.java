package com.bank.front.configs;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestClient;

@Configuration
public class WebClientConfig {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public WebClientConfig(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Bean
    public RestClient gatewayWebClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .requestInterceptor(addAccessTokenHeader())
                .build();
    }

    /**
     * Интерцептор для передачи JWT-токена пользователя в Gateway API
     * Извлекает Access Token из OAuth2AuthorizedClient и добавляет его в заголовок Authorization
     * Access Token содержит информацию о пользователе, ролях и правах, необходимую для Resource Server
     */
    private ClientHttpRequestInterceptor addAccessTokenHeader() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution) throws IOException {
                // Достаём текущую аутентификацию из SecurityContext
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // Проверяем, что пользователь залогинен через OAuth2 (OAuth2AuthenticationToken)
                if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
                    // Из токена берём clientRegistrationId (имя клиента в настройках security)
                    // и имя пользователя (principal), чтобы найти его authorized client
                    OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                            oauth2Token.getAuthorizedClientRegistrationId(),
                            oauth2Token.getName()
                    );

                    // Если для этого пользователя и клиента нашли authorized client —
                    // пробуем достать из него access token (JWT)
                    OAuth2AccessToken accessToken = authorizedClient != null
                            ? authorizedClient.getAccessToken()
                            : null;

                    // Если access token есть, добавляем его в заголовок Authorization
                    if (accessToken != null) {
                        request.getHeaders().setBearerAuth(accessToken.getTokenValue());
                    }
                }

                // Отправляем запрос дальше по цепочке интерцепторов RestClient
                return execution.execute(request, body);
            }
        };
    }
}
