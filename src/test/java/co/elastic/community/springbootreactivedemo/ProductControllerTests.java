package co.elastic.community.springbootreactivedemo;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductController.class)
public class ProductControllerTests {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ProductRepository repository;

    @Test
    public void testRetrieveExistingProduct() {
        Product product = new Product();
        product.setId("123");
        product.setName("My Name");
        product.setDescription("My Description");
        product.setTags(List.of("a", "b", "c"));
        product.setImageUrl("https://example.org");
        when(repository.findById(eq("123"))).thenReturn(Mono.just(product));

        client.get().uri("/products/product/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class).isEqualTo(product);
    }

    @Test
    public void testRetrieveNonExistingProduct() {
        when(repository.findById(eq("123"))).thenReturn(Mono.empty());

        client.get().uri("/products/product/123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentLength(0);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setId("123");
        product.setName("My Name");
        product.setDescription("My Description");
        product.setTags(List.of("a", "b", "c"));
        product.setImageUrl("https://example.org");

        when(repository.save(any())).thenReturn(Mono.just(product));

        client.put().uri("/products/product/123")
                .bodyValue(product)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testCreateProductIdGetsOverwritten() {
        Product returnedProduct = new Product();
        returnedProduct.setId("123");
        returnedProduct.setName("My Name");
        returnedProduct.setDescription("My Description");
        returnedProduct.setTags(List.of("a", "b", "c"));
        returnedProduct.setImageUrl("https://example.org");
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        when(repository.save(productArgumentCaptor.capture())).thenReturn(Mono.just(returnedProduct));

        Product httpRequestProduct = new Product();
        httpRequestProduct.setId("456");
        httpRequestProduct.setName(returnedProduct.getName());
        httpRequestProduct.setDescription(returnedProduct.getDescription());
        httpRequestProduct.setTags(returnedProduct.getTags());
        httpRequestProduct.setImageUrl(returnedProduct.getImageUrl());

        client.put().uri("/products/product/123")
                .bodyValue(httpRequestProduct)
                .exchange()
                .expectStatus().isOk();

        assertThat(productArgumentCaptor.getValue().getId()).isEqualTo("123");
    }

    @Test
    public void testCreateProductNonURL() {
        Product product = new Product();
        product.setId("123");
        product.setName("My Name");
        product.setDescription("My Description");
        product.setTags(List.of("a", "b", "c"));
        product.setImageUrl("invalid-url");

        client.put().uri("/products/product/123")
                .bodyValue(product)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void testDeleteProduct() {
        when(repository.deleteById(eq("123"))).thenReturn(Mono.empty());

        client.delete().uri("/products/product/123")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testSearchProducts() {
        Product p1 = new Product();
        p1.setId("1");
        Product p2 = new Product();
        p2.setId("2");

        when(repository.findProducts(eq("the_query"))).thenReturn(Flux.just(p1, p2));

        client.get().uri("/products/search?q=the_query")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(2)
                .contains(p1, p2);
    }
}