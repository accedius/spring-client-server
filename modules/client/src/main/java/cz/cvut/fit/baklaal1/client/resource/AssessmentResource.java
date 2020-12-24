package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Component
public class AssessmentResource extends BasicResource<AssessmentDTO, AssessmentCreateDTO> {
    private static final String ASSESSMENTS_URL = "/assessments";

    private static final String READ_ALL_BY_EVALUATOR_ID = "/all-by-evaluator-id";

    public AssessmentResource(RestTemplateBuilder builder,
                              @Value( "${cz.cvut.fit.baklaal1.tjv-sem.api.url}" ) String apiUrl) {
        super(builder, apiUrl, ASSESSMENTS_URL, AssessmentDTO.class);
    }

    public Set<AssessmentDTO> readAllByEvaluatorId(String evaluatorId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(READ_ALL_BY_EVALUATOR_ID).queryParam("evaluatorId", evaluatorId);
        Set<AssessmentDTO> evaluatorAssessments = restTemplate.getForObject(uriBuilder.toUriString(), Set.class);
        return evaluatorAssessments;
    }
}
