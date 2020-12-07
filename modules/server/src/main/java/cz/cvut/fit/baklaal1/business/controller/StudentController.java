package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.StudentService;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.StudentModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(StudentController.STUDENT_DOMAIN_ROOT)
public class StudentController extends PersonController<Student, StudentDTO, StudentCreateDTO> {
    public static final String STUDENT_DOMAIN_ROOT = "/students";
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService, StudentModelAssembler studentModelAssembler, PagedResourcesAssembler<Student> pagedResourcesAssembler) {
        super(studentService, studentModelAssembler, pagedResourcesAssembler);
        this.studentService = studentService;
    }
}
