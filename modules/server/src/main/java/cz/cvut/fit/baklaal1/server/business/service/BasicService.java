package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionBuilder;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionInBusinessLogic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Transactional
public abstract class BasicService<T, T_DTO extends RepresentationModel<T_DTO>, T_CREATE_DTO> {
    private final JpaRepository<T, Integer> repository;

    public BasicService(JpaRepository<T, Integer> repository) {
        this.repository = repository;
    }

    public List<T_DTO> findAllAsDTO() {
        return toDTO(findAll());
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Page<T> pageAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Set<T> findAllByIds(Set<Integer> ids) {
        return new TreeSet<>(repository.findAllById(ids));
    }

    public Set<T_DTO> findAllByIdsAsDTO(Set<Integer> ids) {
        return new TreeSet<>(toDTO(repository.findAllById(ids)));
    }

    public Optional<T> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<T_DTO> findByIdAsDTO(Integer id) {
        return toDTO(findById(id));
    }

    public abstract T_DTO create(T_CREATE_DTO itemDTO) throws Exception;

    public abstract T_DTO update(Integer id, T_CREATE_DTO itemDTO) throws Exception;

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    protected abstract boolean exists(T item);

    /**
     * returns an instance of provided E class Exception
     * @param clazz class of custom exception
     * @param duringActionName
     * @param cause
     * @param relatedObject
     * @param <E> custom exception
     * @return instance of provided E class Exception
     */
    protected <E extends Exception> Exception getServiceException(Class<E> clazz, String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder<E> builder = new ServiceExceptionBuilder<>();
        builder.exception().inService(getServiceName()).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    /**
     * returns an instance of ServiceExceptionInBusinessLogic as it is the default
     * @param duringActionName
     * @param cause
     * @param relatedObject
     * @return instance of ServiceExceptionInBusinessLogic
     */
    protected Exception getServiceException(String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder<ServiceExceptionInBusinessLogic> builder = new ServiceExceptionBuilder<ServiceExceptionInBusinessLogic>();
        builder.exception().inService(getServiceName()).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    protected abstract String getServiceName();

    protected abstract T_DTO toDTO(T item);

    protected Optional<T_DTO> toDTO(Optional<T> item) {
        if(item.isEmpty()) return Optional.empty();
        return Optional.of(toDTO(item.get()));
    }

    protected List<T_DTO> toDTO(List<T> items) {
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    protected Set<T_DTO> toDTO(Set<T> items) {
        return items.stream().map(this::toDTO).collect(Collectors.toSet());
    }
}
