package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.server.business.controller.StudentController;
import org.springframework.stereotype.Component;

@Component
public class StudentModelAssembler extends ConvertibleModelAssembler<Student, StudentDTO> {
    public StudentModelAssembler() {
        super(StudentController.class, StudentDTO.class);
    }
}
