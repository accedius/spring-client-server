package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.TeacherService;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.TeacherModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TeacherController.TEACHER_DOMAIN_ROOT)
public class TeacherController extends PersonController<Teacher, TeacherDTO, TeacherCreateDTO> {
    public static final String TEACHER_DOMAIN_ROOT = "/teachers";
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService, TeacherModelAssembler teacherModelAssembler, PagedResourcesAssembler<Teacher> pagedResourcesAssembler) {
        super(teacherService, teacherModelAssembler, pagedResourcesAssembler);
        this.teacherService = teacherService;
    }
}
