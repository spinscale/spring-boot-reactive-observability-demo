package co.elastic.community.springbootreactivedemo;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableScheduling
public class SpringBootApplication {

	public static void main(String[] args) {
		ElasticApmAttacher.attach();
		SpringApplication.run(SpringBootApplication.class, args);
	}

}
