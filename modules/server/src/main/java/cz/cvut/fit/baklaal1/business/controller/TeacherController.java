package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.TeacherService;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeacherController extends PersonController<Teacher, Integer, TeacherDTO, TeacherCreateDTO> {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        super(teacherService);
        this.teacherService = teacherService;
    }
}
