package cz.cvut.fit.baklaal1.client;

import cz.cvut.fit.baklaal1.client.handler.*;
import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.help.ClientAppHelp;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ClientApp implements ApplicationRunner {
	private static final String formatForDate = "HH:mm:ss";

	@Autowired
	private MainHandler handler;

	@Autowired
	private ClientAppHelp appHelp;

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
    	ApplicationArguments argsToHandle = args;
		Scanner in = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatForDate);

		appHelp.printWelcomeMessage();

		while(handler.handleArguments(argsToHandle)) {
			Date date = Calendar.getInstance().getTime();
			System.out.print(dateFormat.format(date) + " Client>");

			String argumentsAsString = in.nextLine();
			String[] argsResource = argumentsAsString.split("\\s+");

			argsToHandle = new DefaultApplicationArguments(argsResource);
		}

		System.out.print("Exiting...");
	}
}
