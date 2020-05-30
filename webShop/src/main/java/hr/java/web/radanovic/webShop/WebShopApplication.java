package hr.java.web.radanovic.webShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("hr.java.web.radanovic.webshop.repository")
@EnableScheduling
public class WebShopApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}

}
