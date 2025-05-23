package jsl.group.catalog_service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresBaseTest {
    private static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>("postgres").withStartupTimeoutSeconds(300);

    static { postgresContainer.start(); }

    @DynamicPropertySource
    public static void setPostgresContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
    }
}
