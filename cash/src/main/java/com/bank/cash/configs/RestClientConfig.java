package com.bank.cash.configs;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Configuration
public class RestClientConfig {
    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient gatewayWebClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .requestInterceptor(addAccessTokenHeader())
                .build();
    }

    private ClientHttpRequestInterceptor addAccessTokenHeader() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution) throws IOException {
                // Достаём текущую аутентификацию из SecurityContext
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // Проверяем, что пользователь залогинен через OAuth2 (OAuth2AuthenticationToken)
                if (authentication instanceof JwtAuthenticationToken accessToken) {
                    // Если access token есть, добавляем его в заголовок Authorization
                    if (accessToken != null) {
                        request.getHeaders().setBearerAuth(accessToken.getToken().getTokenValue());
                    }
                }

                // Отправляем запрос дальше по цепочке интерцепторов RestClient
                return execution.execute(request, body);
            }
        };
    }
}


