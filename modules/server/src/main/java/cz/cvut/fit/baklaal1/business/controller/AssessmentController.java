package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.AssessmentService;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AssessmentController.ASSESSMENT_DOMAIN_ROOT)
public class AssessmentController extends BasicController<Assessment, Integer, AssessmentDTO, AssessmentCreateDTO> {
    public static final String ASSESSMENT_DOMAIN_ROOT = "/assessment";
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService) {
        super(assessmentService);
        this.assessmentService = assessmentService;
    }

    @GetMapping(params = {"evaluatorId"})
    List<AssessmentDTO> allByEvaluatorId(int evaluatorId) {
        return assessmentService.findAllByEvaluatorId(evaluatorId);
    }
}
