package cn.dpc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(controllers = HelloController.class)
class HelloControllerTest {

    @Autowired
    WebTestClient webClient;

    @Test
    @WithMockUser
    void hello() {
        webClient.get()
                .uri(builder -> builder.path("/").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(value -> assertEquals(value, "Hello user!"));
    }
}