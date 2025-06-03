package jsl.group.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

@EnableWebFluxSecurity
public class OrderServiceSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
        httpSecurity.authorizeExchange(authz -> {
            authz.anyExchange().authenticated();
        });
        httpSecurity.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        httpSecurity.requestCache(requestCacheSpec -> {
            requestCacheSpec.requestCache(NoOpServerRequestCache.getInstance());
        });
        return httpSecurity.build();
    }
}
