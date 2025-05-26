package jsl.group.edge_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EdgeServiceApplicationTests {

	private static final int REDIS_PORT = 6379;

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis"))
			.withExposedPorts(REDIS_PORT);

	@DynamicPropertySource
	static void setRedisProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.redis.host", () -> redisContainer.getHost());
		dynamicPropertyRegistry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT));
	}

	@Test
	void contextLoads() {
	}

}
