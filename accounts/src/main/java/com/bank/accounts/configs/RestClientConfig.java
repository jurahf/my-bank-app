package com.bank.accounts.configs;

import java.io.IOException;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient gatewayWebClient(RestClient.Builder restClientBuilder, OAuth2AuthorizedClientManager authorizedClientManager) {
        return restClientBuilder
                .requestInterceptor(addBearerTokenInterceptor(authorizedClientManager))
                .build();
    }

    private ClientHttpRequestInterceptor addBearerTokenInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("account-service")
                    .principal("account-service")
                    .build();
            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

            if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
                request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
            }

            return execution.execute(request, body);
        };
    }


    /**
     * Настраиваем OAuth2AuthorizedClientManager —
     * компонент, который умеет:
     * - создавать сервисный access token по Client Credentials Flow,
     * - обновлять его при истечении,
     * - хранить его в OAuth2AuthorizedClientService.
     * <p>
     * Здесь мы указываем, что accounts использует Client Credentials Flow,
     * поэтому включаем только clientCredentials().
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        // Провайдер, который знает, как получать client_credentials токены
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        // Manager, который связывает client registrations и storage токенов
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService
                );

        // Говорим менеджеру: для transfer-service используем client_credentials
        manager.setAuthorizedClientProvider(authorizedClientProvider);
        return manager;
    }
}
