package co.elastic.community.springbootreactivedemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/")
public class MainController {

    private final ProductRepository repository;

    public MainController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Mono<Rendering> show() {
        final Rendering rendering = Rendering.view("products.html")
                .modelAttribute("products", repository.findAll())
                .build();
        return Mono.just(rendering);
    }

}
