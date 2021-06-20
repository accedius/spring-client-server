package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.help.ClientAppHelp;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class MainHandler {
    private boolean helpIsNeeded;
    private boolean helped;
    private boolean exit;

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

    public boolean handleArguments(ApplicationArguments args) {
        helpIsNeeded = false;
        helped = false;
        exit = false;

        handleOptions(args);

        handleNonOptions(args);

        if(exit) {
            return false;
        }

        if(helpIsNeeded && !helped) {
            System.out.println("Maybe try \"help\"?");
        }

        return true;
    }

    private void handleOptions(ApplicationArguments args) {
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
    }

    private void handleNonOptions(ApplicationArguments args) {
        Set<String> nonOptionsUnique = getNonOptionsSequenceUnique(args.getNonOptionArgs());
        for(String nonOption : nonOptionsUnique) {
            switch (nonOption) {
                case ArgumentConstants.QUIT : {
                    exit = true;
                    return;
                }
                case ArgumentConstants.HELP : {
                    helped = true;
                    appHelp.printHelp();
                    break;
                }
                case ArgumentConstants.DATE : {
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
    }

    private Set<String> getNonOptionsSequenceUnique(Collection<String> nonOptions) {
        Set<String> nonOptionsUnique = new LinkedHashSet<>();

        for(String nonOption : nonOptions) {
            switch (nonOption) {
                case ArgumentConstants.QUIT:
                case ArgumentConstants.Q:
                case ArgumentConstants.EXIT: {
                    nonOptionsUnique.add(ArgumentConstants.QUIT);
                    break;
                }
                case ArgumentConstants.MANUAL:
                case ArgumentConstants.MAN:
                case ArgumentConstants.H:
                case ArgumentConstants.HELP: {
                    nonOptionsUnique.add(ArgumentConstants.HELP);
                    break;
                }
                case ArgumentConstants.DATE:
                case ArgumentConstants.TIME: {
                    nonOptionsUnique.add(ArgumentConstants.DATE);
                    break;
                }
                default: {
                    nonOptionsUnique.add(nonOption);
                }
            }
        }

        return nonOptionsUnique;
    }
}
