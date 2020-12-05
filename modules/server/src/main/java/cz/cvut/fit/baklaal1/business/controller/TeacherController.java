package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.TeacherService;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TeacherController.TEACHER_DOMAIN_ROOT)
public class TeacherController extends PersonController<Teacher, Integer, TeacherDTO, TeacherCreateDTO> {
    public static final String TEACHER_DOMAIN_ROOT = "/teacher";
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        super(teacherService);
        this.teacherService = teacherService;
    }
}
