package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.server.business.controller.AssessmentController;
import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.stereotype.Component;

@Component
public class AssessmentModelAssembler extends ConvertibleModelAssembler<Assessment, AssessmentDTO> {
    public AssessmentModelAssembler() {
        super(AssessmentController.class, AssessmentDTO.class);
    }
}
