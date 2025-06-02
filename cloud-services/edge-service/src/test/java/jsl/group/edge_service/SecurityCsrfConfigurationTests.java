package jsl.group.edge_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.when;

@WebFluxTest(properties = {"spring.cloud.config.enabled=false"})
@Import(EdgeServiceSecurityTests.class)
public class SecurityCsrfConfigurationTests {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void logoutAuthenticatedWith302() {
        when(clientRegistrationRepository.findByRegistrationId("test"))
                .thenReturn(Mono.just(testClientRegistration()));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/logout").exchange().expectStatus().isFound();
    }

    private ClientRegistration testClientRegistration() {
        return ClientRegistration.withRegistrationId("test")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("test")
                .tokenUri("http://token")
                .authorizationUri("https://sso.polar-store-ui.com/auth")
                .redirectUri("https://polar-store.com")
                .build();
    }
}
