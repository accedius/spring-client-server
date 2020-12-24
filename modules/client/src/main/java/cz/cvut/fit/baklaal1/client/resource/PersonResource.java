package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.BasicDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.PersonDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.TreeSet;

public abstract class PersonResource<T_DTO extends PersonDTO<T_DTO>, T_CREATE_DTO> extends BasicResource<T_DTO, T_CREATE_DTO> {
    protected static final String READ_BY_USERNAME = "/by-username";
    protected static final String READ_ALL_BY_NAME = "/all-by-name";

    public PersonResource(RestTemplateBuilder builder, String apiUrl, String classUrl, Class<T_DTO> modelClass) {
        super(builder, apiUrl, classUrl, modelClass);
    }

    public T_DTO readByUsername(String username) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(READ_BY_USERNAME).queryParam("username", username);
        T_DTO person = restTemplate.getForObject(uriBuilder.build().toUriString(), modelClass);
        return person;
    }

    public Set<T_DTO> readAllByName(String name) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(READ_ALL_BY_NAME).queryParam("name", name);
        T_DTO[] personArray = restTemplate.getForObject(uriBuilder.toUriString(), getResponseTypeForArray());
        Set<T_DTO> students = fillCollectionFromArray(new TreeSet<>(), personArray);
        return students;
    }
}
