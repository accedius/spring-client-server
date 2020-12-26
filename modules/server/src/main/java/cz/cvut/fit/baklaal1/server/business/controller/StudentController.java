package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.server.business.service.StudentService;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionInBusinessLogic;
import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.server.data.entity.dto.assembler.StudentModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(StudentController.STUDENT_DOMAIN_ROOT)
public class StudentController extends PersonController<Student, StudentDTO, StudentCreateDTO> {
    public static final String STUDENT_DOMAIN_ROOT = "/students";
    //TODO find out if a mapping could start with something other than a slash "/" to differentiate between sub-paths and path's methods
    public static final String STUDENT_JOIN_WORK = "/join-work";
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService, StudentModelAssembler studentModelAssembler, PagedResourcesAssembler<Student> pagedResourcesAssembler) {
        super(studentService, studentModelAssembler, pagedResourcesAssembler);
        this.studentService = studentService;
    }

    /**
     * Lets Student with a given studentId join the Work with workId
     * @param studentId primary key of the Student joining the Work
     * @param workId primary key of the Work to be joined by the Student
     */
    @RequestMapping(value = STUDENT_JOIN_WORK, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinWork(@RequestParam int studentId, @RequestParam int workId) {
        try {
            studentService.joinWork(studentId, workId);
        } catch (ServiceExceptionInBusinessLogic e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
