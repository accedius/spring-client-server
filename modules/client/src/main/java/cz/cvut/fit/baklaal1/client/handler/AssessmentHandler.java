package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.AssessmentResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class AssessmentHandler extends BasicHandler<AssessmentDTO, AssessmentCreateDTO> {
    @Autowired
    private final AssessmentResource assessmentResource;

    public AssessmentHandler(AssessmentResource assessmentResource) {
        super(assessmentResource);
        this.assessmentResource = assessmentResource;
    }

    @Override
    protected void printModel(AssessmentDTO model) {
        ;
    }

    @Override
    protected boolean checkArgumentsForCreateModel(ApplicationArguments args) {
        return false;
    }

    @Override
    protected AssessmentCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected String getIdFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected void printPagedModel(AssessmentDTO model) {

    }
}
