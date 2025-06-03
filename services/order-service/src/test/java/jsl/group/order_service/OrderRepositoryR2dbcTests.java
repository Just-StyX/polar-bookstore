package jsl.group.order_service;

import jsl.group.order_service.config.DataConfiguration;
import jsl.group.order_service.config.OrderConfigurationProperties;
import jsl.group.order_service.domain.OrderRepository;
import jsl.group.order_service.domain.OrderService;
import jsl.group.order_service.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@DataR2dbcTest(properties = {"spring.cloud.config.enabled=false"})
@Testcontainers
@Import(DataConfiguration.class)
@EnableConfigurationProperties(OrderConfigurationProperties.class)
public class OrderRepositoryR2dbcTests {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres"));

    static {postgreSQLContainer.start();}

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void setPostgreSQLContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl);
        dynamicPropertyRegistry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    }

//    @Test
    void  rejectedOrder() {
        var rejectedOrder = OrderService.buildRejectedOrder("ISBN-10 0-596-52068", 4);
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(
                        order -> order.status().equals(OrderStatus.REJECTED)
                ).verifyComplete();
    }

    private static String r2dbcUrl() {
        return String.format(
                "r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName()
        );
    }
// TODO: Add audit tests
}
