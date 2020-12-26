package cz.cvut.fit.baklaal1.client;

import cz.cvut.fit.baklaal1.client.handler.AssessmentHandler;
import cz.cvut.fit.baklaal1.client.handler.StudentHandler;
import cz.cvut.fit.baklaal1.client.handler.TeacherHandler;
import cz.cvut.fit.baklaal1.client.handler.WorkHandler;
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
	private AssessmentHandler assessmentHandler;

	@Autowired
	private StudentHandler studentHandler;

	@Autowired
	private TeacherHandler teacherHandler;

	@Autowired
	private WorkHandler workHandler;

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

	private boolean handleArguments(ApplicationArguments args) {
    	boolean helpIsNeeded = false;
    	boolean helped = false;

		if (args.containsOption("action") && args.containsOption("entity")) {
			String entity = args.getOptionValues("entity").get(0);
			try {
				switch (entity) {
					case ArgumentConstants.STUDENT: {
						studentHandler.handle(args);
						break;
					}
					case ArgumentConstants.TEACHER: {
						teacherHandler.handle(args);
						break;
					}
					case ArgumentConstants.WORK: {
						workHandler.handle(args);
						break;
					}
					case ArgumentConstants.ASSESSMENT: {
						assessmentHandler.handle(args);
						break;
					}
					default: {
						System.err.println("Invalid entity name: \"" + entity + "\"!");
						helpIsNeeded = true;
						//System.out.println("Maybe try \"help\"?");
					}
				}
			} catch (IllegalArgumentException e) {
				System.err.println("Exception message: " + e.getMessage());
				System.err.println(ExceptionUtils.getStackTrace(e));
				helpIsNeeded = true;
			} catch (Exception e) {
				System.err.println("Exception message: " + e.getMessage());
				System.err.println(ExceptionUtils.getStackTrace(e));
			}
		} else if(args.getNonOptionArgs().size() == 0 && args.getSourceArgs().length > 0) {
			helpIsNeeded = true;
		}

		Set<String> nonOptions = new LinkedHashSet<>(args.getNonOptionArgs());
		for(String nonOption : nonOptions) {
			switch (nonOption) {
				case ArgumentConstants.QUIT :
				case ArgumentConstants.Q :
				case ArgumentConstants.EXIT : {
					return false;
				}
				case ArgumentConstants.MANUAL :
				case ArgumentConstants.MAN :
				case ArgumentConstants.H :
				case ArgumentConstants.HELP : {
					helped = true;
					appHelp.printHelp();
					break;
				}
				case ArgumentConstants.DATE :
				case ArgumentConstants.TIME : {
					System.out.println("Currently it is " + Calendar.getInstance().getTime());
					break;
				}
				case "" : {
					break;
				}
				default: {
					if(!helpIsNeeded && nonOption.matches(".*[a-zA-Z0-9]+.*")) {
						helpIsNeeded = true;
					}
				}
			}
		}

		if(helpIsNeeded && !helped) {
			System.out.println("Maybe try \"help\"?");
		}

		return true;
	}

    @Override
    public void run(ApplicationArguments args) {
    	ApplicationArguments argsToHandle = args;
		Scanner in = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatForDate);

		appHelp.printWelcomeMessage();

		while(handleArguments(argsToHandle)) {
			Date date = Calendar.getInstance().getTime();
			System.out.print(dateFormat.format(date) + " Client>");

			String argumentsAsString = in.nextLine();
			String[] argsResource = argumentsAsString.split("\\s+");

			argsToHandle = new DefaultApplicationArguments(argsResource);
		}

		System.out.print("Exiting...");
	}
}
