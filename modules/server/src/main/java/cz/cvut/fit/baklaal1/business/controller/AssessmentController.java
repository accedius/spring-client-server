package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.AssessmentService;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.AssessmentModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(AssessmentController.ASSESSMENT_DOMAIN_ROOT)
public class AssessmentController extends BasicController<Assessment, AssessmentDTO, AssessmentCreateDTO> {
    public static final String ASSESSMENT_DOMAIN_ROOT = "/assessments";
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService, AssessmentModelAssembler assessmentModelAssembler, PagedResourcesAssembler<Assessment> pagedResourcesAssembler) {
        super(assessmentService, assessmentModelAssembler, pagedResourcesAssembler);
        this.assessmentService = assessmentService;
    }

    @GetMapping(params = {"evaluatorId"})
    public Set<AssessmentDTO> readAllByEvaluatorId(int evaluatorId) {
        Set<AssessmentDTO> assessmentsAsDTO = assessmentService.findAllByEvaluatorId(evaluatorId);
        modelAssembler.addLinksToModels(assessmentsAsDTO);
        return assessmentsAsDTO;
    }
}
