package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.StudentResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class StudentHandler extends BasicHandler<StudentDTO, StudentCreateDTO> {
    @Autowired
    private final StudentResource studentResource;

    public StudentHandler(StudentResource studentResource) {
        super(studentResource);
        this.studentResource = studentResource;
    }

    @Override
    protected void printModel(StudentDTO model) {
        System.out.println(model.getName());
        System.out.println(model.getUsername());
        System.out.println(model.getBirthdate());
        System.out.println(model.getAverageGrade());
        System.out.println(model.getWorkIds());
    }

    @Override
    protected boolean checkArgumentsForCreateModel(ApplicationArguments args) {
        return false;
    }

    @Override
    protected StudentCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected String getIdFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected void printPagedModel(StudentDTO model) {
        ;
    }
}
