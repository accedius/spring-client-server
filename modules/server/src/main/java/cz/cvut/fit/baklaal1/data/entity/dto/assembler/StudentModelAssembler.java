package cz.cvut.fit.baklaal1.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.business.controller.StudentController;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentModelAssembler extends ConvertibleModelAssembler<Student, StudentDTO> {
    public StudentModelAssembler() {
        super(StudentController.class, StudentDTO.class);
    }
}
