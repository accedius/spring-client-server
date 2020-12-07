package cz.cvut.fit.baklaal1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
