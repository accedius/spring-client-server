package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.StudentService;
import cz.cvut.fit.baklaal1.data.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("StudentController Test")
class StudentControllerTest {
    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    void byId() {
        Student student = new Student(1, "baklaal1", "Aleksej", null, 0, null);
        BDDMockito.given(studentService.findById(student.getId())).willReturn(Optional.of(student));

        Assertions.assertEquals(student.getUsername(), studentController.byId(student.getId()).getUsername());
        Mockito.verify(studentService, Mockito.atLeastOnce()).findById(student.getId());
    }

    @Test
    void save() {
    }
}