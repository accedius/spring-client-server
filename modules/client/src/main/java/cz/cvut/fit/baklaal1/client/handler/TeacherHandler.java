package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.TeacherResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class TeacherHandler extends BasicHandler<TeacherDTO, TeacherCreateDTO> {
    @Autowired
    private final TeacherResource teacherResource;

    public TeacherHandler(TeacherResource teacherResource) {
        super(teacherResource);
        this.teacherResource = teacherResource;
    }

    @Override
    protected void printModel(TeacherDTO model) {
        ;
    }

    @Override
    protected boolean checkArgumentsForCreateModel(ApplicationArguments args) {
        return false;
    }

    @Override
    protected TeacherCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected String getIdFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected void printPagedModel(TeacherDTO model) {

    }
}
