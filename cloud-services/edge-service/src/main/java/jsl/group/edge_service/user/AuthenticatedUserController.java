package jsl.group.edge_service.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class AuthenticatedUserController {
    @GetMapping("user")
    public Mono<AuthenticatedUser> authenticatedUserMono(@AuthenticationPrincipal OidcUser oidcUser) {
        return Mono.just(
                new AuthenticatedUser(
                        oidcUser.getPreferredUsername(),
                        oidcUser.getGivenName(),
                        oidcUser.getFamilyName(),
                        oidcUser.getClaimAsStringList("roles")
                )
        );
    }
}
