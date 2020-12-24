package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.helper.ClientAppHelp;
import cz.cvut.fit.baklaal1.client.resource.AssessmentResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AssessmentHandler extends BasicHandler<AssessmentDTO, AssessmentCreateDTO> {
    private static final String EVALUATOR_ID = ArgumentConstants.EVALUATOR_ID;
    private static final String READ_ALL_BY_EVALUATOR_ID = ArgumentConstants.READ_ALL_BY_EVALUATOR_ID;

    @Autowired
    private final AssessmentResource assessmentResource;

    public AssessmentHandler(AssessmentResource assessmentResource) {
        super(assessmentResource);
        this.assessmentResource = assessmentResource;
    }

    @Override
    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = super.handle(args);
        if(wasHandled) {
            return wasHandled;
        }

        String action = args.getOptionValues("action").get(0);
        switch (action) {
            case READ_ALL_BY_EVALUATOR_ID: {
                wasHandled = true;
                try {
                    readAllByEvaluatorId(args);
                } catch (Exception e) {
                    printError(e, READ_ALL_BY_EVALUATOR_ID, args);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("No such action for Assessment: \"" + action + "\"!");
            }
        }

        return wasHandled;
    }

    private void readAllByEvaluatorId(ApplicationArguments args) throws Exception {
        if(!args.containsOption(EVALUATOR_ID)) {
            throwMustContain(EVALUATOR_ID);
        }

        String evaluatorId = args.getOptionValues(EVALUATOR_ID).get(0);
        Set<AssessmentDTO> assessments = assessmentResource.readAllByEvaluatorId(evaluatorId);
        printAll(assessments);
    }

    @Override
    protected AssessmentCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }
}
