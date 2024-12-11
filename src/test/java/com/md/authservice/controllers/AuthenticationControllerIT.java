package com.md.authservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.authservice.dtos.RegisterUserDto;
import com.md.authservice.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIT {

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

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("must register a new user")
    void t1() throws JsonProcessingException {
        // arrange
        final ObjectMapper objectMapper = new ObjectMapper();
        final String urlRequest = String.format("http://localhost:%d/auth/signup", httpPort);
        final RegisterUserDto registerUser = new RegisterUserDto()
                .setFullName("Maycon Braga")
                .setEmail("maycon@gmail.com")
                .setPassword("maycon");

        // act
        final Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .and()
                .body(objectMapper.writeValueAsString(registerUser))
                .when()
                .post(urlRequest)
                .then()
                .extract()
                .response();

        // assert
        Assertions.assertEquals(response.statusCode(), HttpStatus.OK.value());
        Assertions.assertEquals(response.jsonPath().getString("fullName"), registerUser.getFullName());
        Assertions.assertEquals(response.jsonPath().getString("email"), registerUser.getEmail());

        int size = (int) userRepository.findAll().spliterator().getExactSizeIfKnown();
        Assertions.assertEquals(1, size);
    }
}
