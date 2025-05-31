package jsl.group.edge_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@TestConfiguration
public class EdgeServiceSecurityTests {
    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("client")
                .clientId("dummy")
                .scope("scope")
                .clientSecret("secret")
                .redirectUri("http://")
                .tokenUri("http://")
                .authorizationUri("http://")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }
}
