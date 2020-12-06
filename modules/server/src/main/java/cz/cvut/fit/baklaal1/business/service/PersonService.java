package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.PersonRepository;
import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class PersonService<T extends Person, ID, T_DTO extends PersonDTO, T_CREATE_DTO extends PersonCreateDTO> extends BasicService<T, ID, T_DTO, T_CREATE_DTO> {
    private final PersonRepository<T> personRepository;

    public PersonService(PersonRepository<T> repository) {
        //TODO better super call solution
        super((JpaRepository<T, ID>) repository);
        personRepository = repository;
    }

    protected T fillPerson(T person, T_CREATE_DTO itemDTO) throws Exception {
        person.setUsername(itemDTO.getUsername());
        person.setName(itemDTO.getName());
        person.setBirthDate(itemDTO.getBirthDate());
        return person;
    }

    public Optional<T_DTO> findByUsername(String username) {
        return toDTO(personRepository.findByUsername(username));
    }

    public List<T_DTO> findAllByName(String name) {
        List<T> persons = personRepository.findAllByName(name);
        return toDTO(persons);
    }
}
