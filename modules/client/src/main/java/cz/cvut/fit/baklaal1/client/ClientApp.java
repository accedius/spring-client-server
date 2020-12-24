package cz.cvut.fit.baklaal1.client;

import cz.cvut.fit.baklaal1.client.handler.AssessmentHandler;
import cz.cvut.fit.baklaal1.client.handler.StudentHandler;
import cz.cvut.fit.baklaal1.client.handler.TeacherHandler;
import cz.cvut.fit.baklaal1.client.handler.WorkHandler;
import cz.cvut.fit.baklaal1.client.helper.ClientAppHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;

@SpringBootApplication
@EntityScan("cz.cvut.fit.baklaal1.model.data.entity")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ClientApp implements ApplicationRunner {
	@Autowired
	private AssessmentHandler assessmentHandler;

	@Autowired
	private StudentHandler studentHandler;

	@Autowired
	private TeacherHandler teacherHandler;

	@Autowired
	private WorkHandler workHandler;

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(HypermediaRestTemplateConfigurer configurer) {
		return restTemplate -> {
			configurer.registerHypermediaTypes(restTemplate);
		};
	}

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("action") && args.containsOption("entity")) {
        	String entity = args.getOptionValues("entity").get(0);
        	try {
				switch (entity) {
					case "Student": {
						studentHandler.handle(args);
						break;
					}
					case "Teacher": {
						teacherHandler.handle(args);
						break;
					}
					case "Work": {
						workHandler.handle(args);
						break;
					}
					case "Assessment": {
						assessmentHandler.handle(args);
						break;
					}
					default: {
						System.err.println("Invalid entity name: \"" + entity + "\"!");
						ClientAppHelp.printHelp();
					}
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		} else {
        	ClientAppHelp.printHelp();
		}
    }
}
