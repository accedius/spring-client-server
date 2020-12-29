package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.server.business.repository.PersonRepository;
import cz.cvut.fit.baklaal1.entity.Person;
import cz.cvut.fit.baklaal1.model.data.entity.dto.PersonCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.PersonDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
public abstract class PersonService<T extends Person, T_DTO extends PersonDTO<T_DTO>, T_CREATE_DTO extends PersonCreateDTO> extends BasicService<T, T_DTO, T_CREATE_DTO> {
    private final PersonRepository<T> personRepository;

    public PersonService(PersonRepository<T> repository) {
        super(repository);
        personRepository = repository;
    }

    @Override
    protected boolean exists(T item) {
        return personRepository.findByUsername(item.getUsername()).isPresent();
    }

    protected T fillPerson(T person, T_CREATE_DTO itemDTO) throws Exception {
        person.setUsername(itemDTO.getUsername());
        person.setName(itemDTO.getName());
        person.setBirthdate(itemDTO.getBirthdate());
        return person;
    }

    public Optional<T_DTO> findByUsernameAsDTO(String username) {
        return toDTO(personRepository.findByUsername(username));
    }

    public Set<T_DTO> findAllByNameAsDTO(String name) {
        Set<T> persons = personRepository.findAllByName(name);
        return toDTO(persons);
    }

    public void deleteByUsername(String username) {
        Optional<T_DTO> optionalPerson = findByUsernameAsDTO(username);
        if(optionalPerson.isPresent()) {
            int id = optionalPerson.get().getId();
            delete(id);
        }
    }
}
