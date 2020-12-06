package cz.cvut.fit.baklaal1.business.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class BasicService<T, ID, T_DTO, T_CREATE_DTO> {
    private final JpaRepository<T, ID> repository;

    public BasicService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T_DTO> findAll() {
        List<T> items = repository.findAll();
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Set<T> findByIds(Set<ID> ids) {
        return new TreeSet<>(repository.findAllById(ids));
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public Optional<T_DTO> findByIdAsDTO(ID id) {
        return toDTO(findById(id));
    }

    public abstract T_DTO create(T_CREATE_DTO itemDTO) throws Exception;

    public abstract T_DTO update(ID id, T_CREATE_DTO itemDTO) throws Exception;

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    protected abstract T_DTO toDTO(T item);

    protected Optional<T_DTO> toDTO(Optional<T> item) {
        if(item.isEmpty()) return Optional.empty();
        return Optional.of(toDTO(item.get()));
    }

    protected List<T_DTO> toDTO(List<T> items) {
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
