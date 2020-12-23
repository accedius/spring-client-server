package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class WorkResource extends BasicResource<WorkDTO, WorkCreateDTO> {
    private static final String WORKS_URL = "/works";

    public WorkResource(RestTemplateBuilder builder,
                        @Value( "${cz.cvut.fit.baklaal1.server.url}" ) String apiUrl) {
        super(builder, apiUrl, WORKS_URL, WorkDTO.class);
    }
}