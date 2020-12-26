package cz.cvut.fit.baklaal1.client.help;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import org.springframework.stereotype.Component;

@Component
public class ClientAppHelp {
    private static final String helpMessage = "\n" +
            "-------\n" +
            "MANUAL:\n" +
            "-------\n\n" +
            "### Basic commands:\n" +
            "\n" +
            "To get help type in:\n" +
            "\n" +
            "- \"help\"\n" +
            "- \"h\"\n" +
            "- \"manual\"\n" +
            "- \"man\"\n" +
            "\n" +
            "To get current time type in:\n" +
            "\n" +
            "- \"date\"\n" +
            "- \"time\"\n" +
            "\n" +
            "To exit type in:\n" +
            "\n" +
            "- \"exit\"\n" +
            "- \"quit\"\n" +
            "- \"q\"\n" +
            "\n" +
            "### Basic actions:\n" +
            "\n" +
            "- \"--action=create ...\"\n" +
            "- \"--action=read id=<id>\"\n" +
            "- \"--action=update id=<id> ...\"\n" +
            "- \"--action=delete id=<id>\"\n" +
            "- \"--action=readAll\"\n" +
            "- \"--action=pageAll\"\n" +
            "\n" +
            "### Arguments for actions:\n" +
            "\n" +
            "- Use \"--entity=<wanted-entity>\" to operate with entity of type \"<wanted-entity>\"\n" +
            "- Use \"--valueAttribute=<value>\" to pass simple attribute (e.g. Integer, String etc.)\n" +
            "- Use \"--complexAttribute=<value1> --complexAttribute=<value2> ...\" to pass multiple attributes as complex attribute (i.e. authorIds for Work entity etc.)\n" +
            "\n" +
            "### Attributes info:\n" +
            "\n" +
            "- \"*\" means non-required \n" +
            "- \"c\" means complex\n" +
            "\n" +
            "### Available options per entity:\n" +
            "\n" +
            "#### Student:\n" +
            "\n" +
            "Use \"--entity=student\"\n" +
            "\n" +
            "Student's attributes:\n" +
            "\n" +
            "- \"--username\"\n" +
            "- \"--name\"\n" +
            "- \"--birthdate\"*\n" +
            "- \"--averageGrade\"*\n" +
            "- \"--workIds\"*c\n" +
            "\n" +
            "Special actions:\n" +
            "\n" +
            "- \"--action=readByUsername username=<username>\"\n" +
            "- \"--action=readAllByName name=<name>\"\n" +
            "- \"--action=joinWork studentId=<id1> workId=<id2>\"\n" +
            "\n" +
            "#### Teacher:\n" +
            "\n" +
            "Use \"--entity=teacher\"\n" +
            "\n" +
            "Teacher's attributes:\n" +
            "\n" +
            "- \"--username\"\n" +
            "- \"--name\"\n" +
            "- \"--birthdate\"*\n" +
            "- \"--wage\"*\n" +
            "- \"--assessmentIds\"*c\n" +
            "\n" +
            "Special actions:\n" +
            "\n" +
            "- \"--action=readByUsername username=<username>\"\n" +
            "- \"--action=readAllByName name=<name>\"\n" +
            "\n" +
            "#### Work:\n" +
            "\n" +
            "Use \"--entity=work\"\n" +
            "\n" +
            "Work's attributes:\n" +
            "\n" +
            "- \"--title\"\n" +
            "- \"--text\"*\n" +
            "- \"--authorIds\"c\n" +
            "- \"--assessmentId\"*\n" +
            "\n" +
            "Special actions:\n" +
            "\n" +
            "- \"--action=readAllByTitle title=<title>\"\n" +
            "\n" +
            "#### Assessment:\n" +
            "\n" +
            "Use \"--entity=assessment\"\n" +
            "\n" +
            "Assessment's attributes:\n" +
            "\n" +
            "- \"--grade\"\n" +
            "- \"--workId\"\n" +
            "- \"--evaluatorId\"\n" +
            "\n" +
            "Special actions:\n" +
            "\n" +
            "- \"--action=readAllByEvaluatorId evaluatorId=<evaluatorId>\"\n";

    public void printHelp() {
        System.out.println(helpMessage);
    }

    public void printWelcomeMessage() {
        System.out.println("Welcome to the client application!");
        System.out.println("Made by Aleksei Baklanov");
        System.out.println("Contact me at \"baklaal1@fit.cvut.cz\"");
        System.out.println();
        System.out.println("For help type in \"" + ArgumentConstants.HELP + "\" or simply \"" + ArgumentConstants.H + "\"");
        System.out.println();
    }
}
