package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Component
public class WorkResource extends BasicResource<WorkDTO, WorkCreateDTO> {
    private static final String WORKS_URL = "/works";

    public static final String READ_ALL_BY_TITLE = "/all-by-title";

    public WorkResource(RestTemplateBuilder builder,
                        @Value( "${cz.cvut.fit.baklaal1.tjv-sem.api.url}" ) String apiUrl) {
        super(builder, apiUrl, WORKS_URL, WorkDTO.class);
    }

    public Set<WorkDTO> readAllByTitle(String title) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(READ_ALL_BY_TITLE).queryParam("title", title);
        Set<WorkDTO> works = restTemplate.getForObject(uriBuilder.toUriString(), Set.class);
        return works;
    }
}