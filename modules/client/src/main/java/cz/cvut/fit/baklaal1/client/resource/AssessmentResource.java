package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.TreeSet;

@Component
public class AssessmentResource extends BasicResource<AssessmentDTO, AssessmentCreateDTO> {
    private static final String ASSESSMENTS_URL = "/assessments";

    private static final String READ_ALL_BY_EVALUATOR_ID = "/all-by-evaluator-id";

    public AssessmentResource(RestTemplateBuilder builder,
                              @Value( "${cz.cvut.fit.baklaal1.tjv-sem.api.url}" ) String apiUrl) {
        super(builder, apiUrl, ASSESSMENTS_URL, AssessmentDTO.class);
    }

    public Set<AssessmentDTO> readAllByEvaluatorId(String evaluatorId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(READ_ALL_BY_EVALUATOR_ID).queryParam("evaluatorId", evaluatorId);
        AssessmentDTO[] assessmentArray = restTemplate.getForObject(uriBuilder.build(false).toUriString(), getResponseTypeForArray());
        Set<AssessmentDTO> evaluatorAssessments = fillCollectionFromArray(new TreeSet<>(), assessmentArray);
        return evaluatorAssessments;
    }

    @Override
    protected ParameterizedTypeReference<PagedModel<AssessmentDTO>> getParametrizedTypeReference() {
        return new ParameterizedTypeReference<PagedModel<AssessmentDTO>>() {};
    }

    @Override
    protected Class<AssessmentDTO[]> getResponseTypeForArray() {
        return AssessmentDTO[].class;
    }
}
