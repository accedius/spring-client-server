package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.server.business.service.AssessmentService;
import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.server.data.entity.dto.assembler.AssessmentModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(AssessmentController.ASSESSMENT_DOMAIN_ROOT)
public class AssessmentController extends BasicController<Assessment, AssessmentDTO, AssessmentCreateDTO> {
    public static final String ASSESSMENT_DOMAIN_ROOT = "/assessments";
    public static final String READ_ALL_BY_EVALUATOR_ID = "/all-by-evaluator-id";
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService, AssessmentModelAssembler assessmentModelAssembler, PagedResourcesAssembler<Assessment> pagedResourcesAssembler) {
        super(assessmentService, assessmentModelAssembler, pagedResourcesAssembler);
        this.assessmentService = assessmentService;
    }

    @RequestMapping(value = READ_ALL_BY_EVALUATOR_ID, method = RequestMethod.GET)
    public Set<AssessmentDTO> readAllByEvaluatorId(int evaluatorId) {
        Set<AssessmentDTO> assessmentsAsDTO = assessmentService.findAllByEvaluatorIdAsDTO(evaluatorId);
        modelAssembler.addLinksToModels(assessmentsAsDTO);
        return assessmentsAsDTO;
    }
}
