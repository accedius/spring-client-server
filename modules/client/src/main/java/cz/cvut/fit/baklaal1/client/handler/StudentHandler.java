package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.StudentResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class StudentHandler extends PersonHandler<StudentDTO, StudentCreateDTO> {
    private static final String JOIN_WORK = ArgumentConstants.JOIN_WORK;
    public static final String STUDENT_ID = ArgumentConstants.STUDENT_ID;
    public static final String WORK_ID = ArgumentConstants.WORK_ID;

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
    protected StudentCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }
}
