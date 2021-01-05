package cz.cvut.fit.baklaal1.client;

import cz.cvut.fit.baklaal1.client.handler.MainHandler;
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
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ClientApp implements ApplicationRunner {
	private static final String formatForDate = "HH:mm:ss";
	private static boolean welcomed = false;

	@Autowired
	private MainHandler handler;

	@Autowired
	private ClientAppHelp appHelp;

    public static void main(String[] args) {
    	//TODO do better, see https://stackoverflow.com/questions/4159802/how-can-i-restart-a-java-application
		try {
        	SpringApplication.run(ClientApp.class, args);
		} catch (NoSuchElementException e) {
			return;
		} catch (IllegalStateException e) {
			System.err.println("Error on parsing given line!");
			System.err.println("Exception message: " + e.getMessage());
			System.err.println(ExceptionUtils.getStackTrace(e));
			main(new String[]{"helpNeeded"});
		}
    }

    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(HypermediaRestTemplateConfigurer configurer) {
		return restTemplate -> {
			configurer.registerHypermediaTypes(restTemplate);
		};
	}

	//TODO implement new argument for script passing instead of dealing with redirection from "< script.txt", which causes the sout stacking problem: on script input where are no keyboard interactions, thus no "enter" is being hit, so the new line does not appear, so "some-time Client>" messages stack up in just one row
    @Override
    public void run(ApplicationArguments args) {
    	ApplicationArguments argsToHandle = args;
		Scanner in = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatForDate);

		if(!welcomed) {
			appHelp.printWelcomeMessage();
			welcomed = true;
		}

		while(handler.handleArguments(argsToHandle)) {
			Date date = Calendar.getInstance().getTime();
			System.out.print(dateFormat.format(date) + " Client>");

			if(!in.hasNextLine()) {
				System.out.println();
				break;
			}
			String argumentsAsString = in.nextLine();
			String[] argsResource = parseArguments(argumentsAsString);

			argsToHandle = new DefaultApplicationArguments(argsResource);
		}

		System.out.print("Exiting...");
	}

	private String[] parseArguments(final String source) {
		final String argumentRegex = "([-a-zA-z][^ ]*=\"[^\"]*\")|([-a-zA-z][^ ]*)";
		String[] arguments = Pattern.compile(argumentRegex).matcher(source).results().map(MatchResult::group).toArray(String[]::new);

		//Delete all the commas, DefaultApplicationArguments views commas as literals, so comma stacking phenomenon appears: on input "\"abc\"" it will save as literally "\"abc\"" and when will be displayed as "\"\"abc\"\""
		final String commaRegex = "\"";
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = arguments[i].replaceAll(commaRegex, "");
		}
		return arguments;
	}
}
