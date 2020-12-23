package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Component
public class StudentResource extends BasicResource<StudentDTO, StudentCreateDTO> {
    private static final String STUDENTS_URL = "/students";
    private static final String READ_BY_USERNAME_URL = "";
    private static final String READ_ALL_BY_NAME_URL = "";

    public StudentResource(RestTemplateBuilder builder,
                           @Value( "${cz.cvut.fit.baklaal1.server.url}" ) String apiUrl) {
        super(builder, apiUrl, STUDENTS_URL, StudentDTO.class);
    }

    public StudentDTO readByUsername(String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(READ_BY_USERNAME_URL).queryParam("username", username);
        StudentDTO student = restTemplate.getForObject(builder.build().toUriString(), StudentDTO.class);
        return student;
    }

    public Set<StudentDTO> readAllByName(String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(READ_ALL_BY_NAME_URL).queryParam("name", name);
        Set<StudentDTO> students = restTemplate.getForObject(builder.build().toUriString(), Set.class);
        return students;
    }

    /*public URI create(StudentCreateDTO data) {
        return restTemplate.postForLocation("/", data);
    }

    public StudentDTO read(String id) {
        return restTemplate.getForObject(URI_TEMPLATE_ONE, StudentDTO.class, id);
    }

    public List<StudentDTO> readAll() {
        List<StudentDTO> allStudents = restTemplate.getForObject("/", List.class);
        return allStudents;
    }

    public PagedModel<StudentDTO> pageAll(int page, int size) {
        ResponseEntity<PagedModel<StudentDTO>> response = restTemplate.exchange(
                COLLECTION_TEMPLATED,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<StudentDTO>>() {},
                page,
                size
        );
        return response.getBody();
    }
    public void update(String id, StudentCreateDTO data) {
        restTemplate.put(URI_TEMPLATE_ONE, data, id);
    }

    public void delete(String id) {
        restTemplate.delete(URI_TEMPLATE_ONE, id);
    }*/
}
