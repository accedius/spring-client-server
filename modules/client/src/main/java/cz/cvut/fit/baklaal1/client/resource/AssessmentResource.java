package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AssessmentResource extends BasicResource<AssessmentDTO, AssessmentCreateDTO> {
    private static final String ASSESSMENTS_URL = "/assessments";

    public AssessmentResource(RestTemplateBuilder builder,
                              @Value( "${cz.cvut.fit.baklaal1.server.url}" ) String apiUrl) {
        super(builder, apiUrl, ASSESSMENTS_URL, AssessmentDTO.class);
    }
}
