package jsl.group.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableR2dbcAuditing
public class DataConfiguration {
    @Bean
    WebClient webClient(OrderConfigurationProperties orderConfigurationProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(orderConfigurationProperties.getCatalogService()).build();
    }
}
