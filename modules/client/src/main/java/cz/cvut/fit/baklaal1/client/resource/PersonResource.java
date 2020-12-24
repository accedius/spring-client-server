package cz.cvut.fit.baklaal1.client.resource;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

public abstract class PersonResource<T_DTO, T_CREATE_DTO> extends BasicResource<T_DTO, T_CREATE_DTO> {
    protected static final String READ_BY_USERNAME = "/by-username";
    protected static final String READ_ALL_BY_NAME = "/all-by-name";

    public PersonResource(RestTemplateBuilder builder, String apiUrl, String classUrl, Class<T_DTO> modelClass) {
        super(builder, apiUrl, classUrl, modelClass);
    }

    public T_DTO readByUsername(String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(READ_BY_USERNAME).queryParam("username", username);
        T_DTO student = restTemplate.getForObject(builder.build().toUriString(), modelClass);
        return student;
    }

    public Set<T_DTO> readAllByName(String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(READ_ALL_BY_NAME).queryParam("name", name);
        Set<T_DTO> students = restTemplate.getForObject(builder.build().toUriString(), Set.class);
        return students;
    }
}
