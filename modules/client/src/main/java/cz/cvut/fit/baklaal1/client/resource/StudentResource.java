package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class StudentResource extends PersonResource<StudentDTO, StudentCreateDTO> {
    private static final String STUDENTS_URL = "/students";

    public static final String STUDENT_JOIN_WORK = "/join-work";

    public StudentResource(RestTemplateBuilder builder,
                           @Value( "${cz.cvut.fit.baklaal1.tjv-sem.api.url}" ) String apiUrl) {
        super(builder, apiUrl, STUDENTS_URL, StudentDTO.class);
    }

    public void joinWork(String studentId, String workId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(STUDENT_JOIN_WORK)
                .queryParam("studentId", studentId)
                .queryParam("workId", workId);
        restTemplate.put(uriBuilder.build(false).toUriString(), null);
    }

    @Override
    protected ParameterizedTypeReference<PagedModel<StudentDTO>> getParametrizedTypeReference() {
        return new ParameterizedTypeReference<PagedModel<StudentDTO>>() {};
    }

    @Override
    protected Class<StudentDTO[]> getResponseTypeForArray() {
        return StudentDTO[].class;
    }
}
