package cz.cvut.fit.baklaal1.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EntityScan({"cz.cvut.fit.baklaal1.model.data.entity", "cz.cvut.fit.baklaal1.entity"})
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
