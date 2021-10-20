package co.elastic.community.springbootreactivedemo;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/*
 * A controller expecting to receive JSON to manage products in the Elasticsearch Index
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/product/{id}")
    public Mono<Product> createProduct(@PathVariable String id,
                                              @RequestBody Product product) throws MalformedURLException {
        product.setId(id);
        product.setCreated(ZonedDateTime.now(ZoneOffset.UTC));
        // cheap parsing check, this fails with an exception, if the product image url cannot be parsed!
        // could be done via annotation validation as well
        new URL(product.getImageUrl());
        return repository.save(product);
    }

    @GetMapping(value = "/product/{id}")
    public Mono<Product> retrieveProduct(@PathVariable String id) {
        // this does not return a 404 when the Mono is empty... weird default mode IMO
        return repository.findById(id);
    }

    @DeleteMapping("/product/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return repository.deleteById(id);
    }

    @GetMapping(value = "/search")
    public Flux<Product> search(@RequestParam String q) {
        final Span span = ElasticApm.currentTransaction().startSpan();
        span.setName("my name");
        return repository.findProducts(q).doOnTerminate(span::end);
    }
}
