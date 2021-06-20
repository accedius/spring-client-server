package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
public class TeacherResource extends PersonResource<TeacherDTO, TeacherCreateDTO> {
    private static final String TEACHERS_URL = "/teachers";

    public TeacherResource(RestTemplateBuilder builder,
                           @Value( "${cz.cvut.fit.baklaal1.tjv-sem.api.url}" ) String apiUrl) {
        super(builder, apiUrl, TEACHERS_URL, TeacherDTO.class);
    }

    @Override
    protected ParameterizedTypeReference<PagedModel<TeacherDTO>> getParametrizedTypeReference() {
        return new ParameterizedTypeReference<PagedModel<TeacherDTO>>() {};
    }

    @Override
    protected Class<TeacherDTO[]> getResponseTypeForArray() {
        return TeacherDTO[].class;
    }
}