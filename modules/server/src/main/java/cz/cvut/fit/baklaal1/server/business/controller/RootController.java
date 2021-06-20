package cz.cvut.fit.baklaal1.server.business.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//TODO maybe add more roots later
@RequestMapping(value = {"/"})
public class RootController {
    @GetMapping
    public RepresentationModel rootWithLinks() {
        return new RepresentationModel().add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AssessmentController.class).readPage(0, 10)).withRel(AssessmentController.ASSESSMENT_DOMAIN_ROOT),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StudentController.class).readPage(0, 10)).withRel(StudentController.STUDENT_DOMAIN_ROOT),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeacherController.class).readPage(0, 10)).withRel(TeacherController.TEACHER_DOMAIN_ROOT),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WorkController.class).readPage(0, 10)).withRel(WorkController.WORK_DOMAIN_ROOT)
        );
    }
}
