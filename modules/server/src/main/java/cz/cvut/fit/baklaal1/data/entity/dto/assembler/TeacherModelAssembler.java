package cz.cvut.fit.baklaal1.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.business.controller.TeacherController;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import org.springframework.stereotype.Component;

@Component
public class TeacherModelAssembler extends ConvertibleModelAssembler<Teacher, TeacherDTO> {
    public TeacherModelAssembler() {
        super(TeacherController.class, TeacherDTO.class);
    }
}
