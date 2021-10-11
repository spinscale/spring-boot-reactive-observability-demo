package co.elastic.community.springbootreactivedemo;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveSortingRepository<Product, String> {

    @Query("""
{
  "bool" : {
    "must" : [ { "multi_match" : { "query": "?0", "fields": [ "description", "name", "tags" ] } } ],
    "should" : [ { "match" : { "name" : "?0" } } ]
  }
}
""")
    Flux<Product> findProducts(String q);
}
