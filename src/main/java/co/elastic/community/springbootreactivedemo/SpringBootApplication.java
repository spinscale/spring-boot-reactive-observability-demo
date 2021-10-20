package co.elastic.community.springbootreactivedemo;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

	public static void main(String[] args) {
		ElasticApmAttacher.attach();
		SpringApplication.run(SpringBootApplication.class, args);
	}

}
