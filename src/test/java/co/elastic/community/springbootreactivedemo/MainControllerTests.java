package co.elastic.community.springbootreactivedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MainController.class)
public class MainControllerTests {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ProductRepository repository;

    @Test
    public void testLoadMainPage() {
        Product p1 = new Product();
        p1.setId("1");
        p1.setName("First Name");
        p1.setDescription("First Description");
        p1.setTags(List.of("a", "b", "c"));
        p1.setImageUrl("https://example.org");

        Product p2 = new Product();
        p2.setId("2");
        p2.setName("Second Name");
        p2.setDescription("Second Description");

        when(repository.findAll()).thenReturn(Flux.just(p1, p2));

        client.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).contains("href=\"/products/product/1");
                    assertThat(exchangeResult.getResponseBody()).contains("href=\"/products/product/2");
                    assertThat(exchangeResult.getResponseBody()).contains("src=\"https://example.org");
                    assertThat(exchangeResult.getResponseBody()).contains(p1.getName());
                    assertThat(exchangeResult.getResponseBody()).contains(p1.getDescription());
                    assertThat(exchangeResult.getResponseBody()).contains(p2.getName());
                    assertThat(exchangeResult.getResponseBody()).contains(p2.getDescription());
                });
    }

    @Test
    public void testLoadMainPageWithNoProducts() {
        when(repository.findAll()).thenReturn(Flux.empty());

        client.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).doesNotContain("<table");
                    assertThat(exchangeResult.getResponseBody()).doesNotContain("</table>");
                });
    }

    @Test
    public void testException() {
        client.get().uri("/exception")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).contains("\"timestamp\":"));
    }
}
