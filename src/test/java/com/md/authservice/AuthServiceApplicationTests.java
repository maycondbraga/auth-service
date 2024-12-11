package com.md.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceApplicationTests {
    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0")
            .withDatabaseName("authdb")
            .withUsername("root")
            .withPassword("secret");

    @DynamicPropertySource
    static void propertyConfiguration(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", MY_SQL_CONTAINER::getDriverClassName);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.open-in-view", () -> false);
    }

    @LocalServerPort
    private Integer httpPort;

    @Test
    void contextLoads() {
    }
}
