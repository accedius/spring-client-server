package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.server.business.controller.TeacherController;
import cz.cvut.fit.baklaal1.model.data.entity.Teacher;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.stereotype.Component;

@Component
public class TeacherModelAssembler extends ConvertibleModelAssembler<Teacher, TeacherDTO> {
    public TeacherModelAssembler() {
        super(TeacherController.class, TeacherDTO.class);
    }
}
