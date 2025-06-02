package jsl.group.edge_service;

import jsl.group.edge_service.config.EdgeServiceSecurityConfig;
import jsl.group.edge_service.user.AuthenticatedUser;
import jsl.group.edge_service.user.AuthenticatedUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(EdgeServiceSecurityConfig.class)
@WebFluxTest(controllers = AuthenticatedUserController.class, properties = {"spring.cloud.config.enabled=false"})
public class UserControllerTests {
    @Autowired
    WebTestClient webTestClient;
    @MockitoBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void unauthenticated401() {
        webTestClient.get().uri("/user").exchange().expectStatus().isUnauthorized();
    }

    @Test
    void authentication() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("jsl.group", "JSL", "Group", List.of("employee", "customer"));
        webTestClient
                .mutateWith(oidcLoginMutator(authenticatedUser))
                .get().uri("/user").exchange().expectStatus().is2xxSuccessful().expectBody(AuthenticatedUser.class)
                .value(user -> assertThat(user).isEqualTo(authenticatedUser));
    }

    private SecurityMockServerConfigurers.OidcLoginMutator oidcLoginMutator(AuthenticatedUser authenticatedUser) {
        return SecurityMockServerConfigurers.mockOidcLogin().idToken(builder -> {
            builder.claim(StandardClaimNames.PREFERRED_USERNAME, authenticatedUser.username());
            builder.claim(StandardClaimNames.GIVEN_NAME, authenticatedUser.firstname());
            builder.claim(StandardClaimNames.FAMILY_NAME, authenticatedUser.lastname());
        });
    }
}
