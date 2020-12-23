package cz.cvut.fit.baklaal1.client.resource;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

public abstract class BasicResource<T_DTO, T_CREATE_DTO> {
    protected final RestTemplate restTemplate;

    protected static final String URI_TEMPLATE_ONE = "/{id}";
    protected static final String COLLECTION_TEMPLATED = "/?page={page}&size={size}";

    private final Class<T_DTO> modelClass;

    public BasicResource(RestTemplateBuilder builder, String apiUrl, String classUrl, Class<T_DTO> modelClass) {
        restTemplate = builder.rootUri(apiUrl + classUrl).build();
        this.modelClass = modelClass;
    }

    public URI create(T_CREATE_DTO data) {
        return restTemplate.postForLocation("/", data);
    }

    public T_DTO read(String id) {
        return restTemplate.getForObject(URI_TEMPLATE_ONE, modelClass, id);
    }

    public void update(String id, T_CREATE_DTO data) {
        restTemplate.put(URI_TEMPLATE_ONE, data, id);
    }

    public void delete(String id) {
        restTemplate.delete(URI_TEMPLATE_ONE, id);
    }

    public List<T_DTO> readAll() {
        List<T_DTO> allStudents = restTemplate.getForObject("/", List.class);
        return allStudents;
    }

    public PagedModel<T_DTO> pageAll(int page, int size) {
        ResponseEntity<PagedModel<T_DTO>> response = restTemplate.exchange(
                COLLECTION_TEMPLATED,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<T_DTO>>() {},
                page,
                size
        );
        return response.getBody();
    }
}
