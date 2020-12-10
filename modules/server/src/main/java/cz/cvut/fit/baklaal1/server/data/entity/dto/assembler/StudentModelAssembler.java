package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.server.business.controller.StudentController;
import cz.cvut.fit.baklaal1.model.data.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentModelAssembler extends ConvertibleModelAssembler<Student, StudentDTO> {
    public StudentModelAssembler() {
        super(StudentController.class, StudentDTO.class);
    }
}
