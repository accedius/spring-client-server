package cz.cvut.fit.baklaal1.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public abstract class BasicService<T, ID, T_DTO, T_CREATE_DTO> {
    //TODO find a nicer way to propagate repository and retain @Autowired
    //generic REPO extends JpaRepository<T, ID> is not recognised by @Autowired
    //protected <R extends JpaRepository<T, ID> > R repository;
    //protected final JpaRepository<T, ID> repository;
    private final JpaRepository<T, ID> repository;

    @Autowired
    public BasicService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T_DTO> findAll() {
        List<T> items = repository.findAll();
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<T> findByIds(List<ID> ids) {
        return repository.findAllById(ids);
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
