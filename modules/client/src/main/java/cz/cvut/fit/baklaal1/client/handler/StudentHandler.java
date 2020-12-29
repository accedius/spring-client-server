package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.StudentResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class StudentHandler extends PersonHandler<StudentDTO, StudentCreateDTO> {
    private static final String JOIN_WORK = ArgumentConstants.JOIN_WORK;
    private static final String STUDENT_ID = ArgumentConstants.STUDENT_ID;
    private static final String WORK_ID = ArgumentConstants.WORK_ID;

    private static final String BIRTHDATE = ArgumentConstants.BIRTHDATE;
    private static final String AVERAGE_GRADE = ArgumentConstants.AVERAGE_GRADE;
    private static final String WORK_IDS = ArgumentConstants.WORK_IDS;

    @Autowired
    private final StudentResource studentResource;

    public StudentHandler(StudentResource studentResource) {
        super(studentResource);
        this.studentResource = studentResource;
    }

    @Override
    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = super.handle(args);
        if(wasHandled) {
            return wasHandled;
        }

        String action = args.getOptionValues("action").get(0);
        switch (action) {
            case JOIN_WORK: {
                wasHandled = true;
                try {
                    joinWork(args);
                } catch (Exception e) {
                    printError(e, JOIN_WORK, args);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("No such action for Student: \"" + action + "\"!");
            }
        }

        return wasHandled;
    }

    private void joinWork(ApplicationArguments args) throws Exception {
        if(!args.containsOption(STUDENT_ID) || !args.containsOption(WORK_ID)) {
            throwMustContain(STUDENT_ID, WORK_ID);
        }

        String studentId = args.getOptionValues(STUDENT_ID).get(0);
        String workId = args.getOptionValues(WORK_ID).get(0);
        studentResource.joinWork(studentId, workId);
    }

    @Override
    protected StudentCreateDTO makeCreateModelFromArguments(ApplicationArguments args) throws Exception {
        if(!args.containsOption(USERNAME) || !args.containsOption(NAME)) {
            throwMustContain(USERNAME, NAME);
        }

        String username = args.getOptionValues(USERNAME).get(0);
        String name = args.getOptionValues(NAME).get(0);
        Timestamp birthdate = args.getOptionValues(BIRTHDATE) != null ? new Timestamp(Integer.parseInt(args.getOptionValues(BIRTHDATE).get(0))) : null;

        String averageGradeString = args.getOptionValues(AVERAGE_GRADE) != null ? args.getOptionValues(AVERAGE_GRADE).get(0) : "0";
        //TODO Number locales are hell
        averageGradeString = averageGradeString.replaceAll(",", ".");
        float averageGrade = Float.parseFloat(averageGradeString);

        Set<Integer> workIds = args.getOptionValues(WORK_IDS) != null ? args.getOptionValues(WORK_IDS).stream().map(Integer::parseInt).distinct().collect(Collectors.toCollection(TreeSet::new)) : new TreeSet<>();
        return new StudentCreateDTO(username, name, birthdate, averageGrade, workIds);
    }
}
