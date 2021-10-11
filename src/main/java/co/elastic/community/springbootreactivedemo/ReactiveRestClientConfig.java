package co.elastic.community.springbootreactivedemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;

import java.net.InetSocketAddress;
import java.net.URI;

@Configuration
public class ReactiveRestClientConfig extends AbstractReactiveElasticsearchConfiguration {
    @Override
    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {

        final String elasticsearchUrl = System.getenv("ELASTICSEARCH_URL");
        if (elasticsearchUrl != null) {
            final URI uri = URI.create(elasticsearchUrl);
            final int port = uri.getPort();
            final String host = uri.getHost();

            final InetSocketAddress addr = new InetSocketAddress(host, port);
            final ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder().connectedTo(addr);
            if (uri.getScheme().equals("https")) {
                builder.usingSsl();
            }

            if (uri.getUserInfo() != null) {
                final String[] data = uri.getUserInfo().split(":", 2);
                builder.withBasicAuth(data[0], data[1]);
            }

            return ReactiveRestClients.create(builder.build());
        } else {
            // fallback to localhost:9200 as default, with SSL
            final InetSocketAddress addr = new InetSocketAddress("localhost", 9200);
            final ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder().connectedTo(addr);
            return ReactiveRestClients.create(builder.build());
        }
    }
}
