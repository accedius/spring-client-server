package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.controller.helper.ControllerConstants;
import cz.cvut.fit.baklaal1.business.service.AssessmentService;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AssessmentController extends BasicController<Assessment, Integer, AssessmentDTO, AssessmentCreateDTO> {
    private final AssessmentService assessmentService;
    private static final String domainRoot = ControllerConstants.ASSESSMENT_DOMAIN_ROOT;

    @Autowired
    public AssessmentController(AssessmentService assessmentService) {
        super(assessmentService);
        this.assessmentService = assessmentService;
    }

    @GetMapping(value = domainRoot, params = {"evaluatorId"})
    List<AssessmentDTO> allByEvaluatorId(int evaluatorId) {
        return assessmentService.findAllByEvaluatorId(evaluatorId);
    }

    List<AssessmentDTO> all() {
        return super.all(domainRoot);
    }

    AssessmentDTO byId(int id) {
        return super.byId(domainRoot, id);
    }

    AssessmentDTO save(AssessmentCreateDTO assessmentDTO) throws Exception {
        return super.save(domainRoot, assessmentDTO);
    }

    AssessmentDTO save(int id, AssessmentCreateDTO book) throws Exception {
        return super.save(domainRoot, id, book);
    }

    /*@GetMapping(value = domainRoot, params = {"evaluatorId"})
    List<AssessmentDTO> byTitle(@RequestParam int evaluatorId) {
        return assessmentService.findAllByEvaluatorId(evaluatorId);
    }*/
}
