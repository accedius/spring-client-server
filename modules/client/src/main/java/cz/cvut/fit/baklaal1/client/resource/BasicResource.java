package cz.cvut.fit.baklaal1.client.resource;

import cz.cvut.fit.baklaal1.model.data.entity.dto.BasicDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BasicResource<T_DTO extends BasicDTO<T_DTO>, T_CREATE_DTO> {
    protected final RestTemplate restTemplate;

    protected static final String URI_TEMPLATE_ONE = "/{id}";
    protected static final String COLLECTION_TEMPLATED = "/?page={page}&size={size}";

    protected final String RESOURCE_URL;

    protected final Class<T_DTO> modelClass;

    public BasicResource(RestTemplateBuilder builder, String apiUrl, String classUrl, Class<T_DTO> modelClass) {
        restTemplate = builder.rootUri(apiUrl + classUrl).build();
        this.RESOURCE_URL = apiUrl + classUrl;
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

    protected abstract Class<T_DTO[]> getResponseTypeForArray();

    protected List<T_DTO> convertArrayToList(final T_DTO[] array) {
        if(array == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(array);
    }

    protected List<T_DTO> convertArrayToSet(final T_DTO[] array) {
        if(array == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(array);
    }

    protected <C extends Collection<T_DTO>> C fillCollectionFromArray(C collection, final T_DTO[] array) {
        collection.addAll(Arrays.asList(array));
        return collection;
    }

    public List<T_DTO> readAll() {
        /*Returns List of Linked Hash Maps
        List<T_DTO> allModels = restTemplate.getForObject("/all", List.class);
        return  allModels;*/

        T_DTO[] allModelsAsArray = restTemplate.getForObject("/all", getResponseTypeForArray());
        List<T_DTO> allModels = fillCollectionFromArray(new ArrayList<>(), allModelsAsArray);
        return allModels;
    }

    protected abstract ParameterizedTypeReference<PagedModel<T_DTO>> getParametrizedTypeReference();

    public PagedModel<T_DTO> pageAll(int page, int size) {
        ResponseEntity<PagedModel<T_DTO>> response = restTemplate.exchange(
                COLLECTION_TEMPLATED,
                HttpMethod.GET,
                null,
                getParametrizedTypeReference(),
                page,
                size
        );
        return response.getBody();
    }
}
