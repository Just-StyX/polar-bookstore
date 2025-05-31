package jsl.group.edge_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class EdgeServiceSecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        httpSecurity.authorizeExchange(authz -> {
            authz.pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll();
            authz.pathMatchers(HttpMethod.GET, "/books/**").permitAll();
            authz.anyExchange().authenticated();
        });
        httpSecurity.exceptionHandling(exceptionHandlingSpec -> {
            exceptionHandlingSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
        });
        httpSecurity.oauth2Client(Customizer.withDefaults());
        httpSecurity.logout(logoutSpec -> logoutSpec.logoutSuccessHandler(
                serverLogoutSuccessHandler(clientRegistrationRepository)
        ));
        return httpSecurity.build();
    }

    private ServerLogoutSuccessHandler serverLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var logoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return logoutSuccessHandler;
    }
}
