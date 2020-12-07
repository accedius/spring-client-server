package cz.cvut.fit.baklaal1.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.business.controller.AssessmentController;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import org.springframework.stereotype.Component;

@Component
public class AssessmentModelAssembler extends ConvertibleModelAssembler<Assessment, AssessmentDTO> {
    public AssessmentModelAssembler() {
        super(AssessmentController.class, AssessmentDTO.class);
    }
}
