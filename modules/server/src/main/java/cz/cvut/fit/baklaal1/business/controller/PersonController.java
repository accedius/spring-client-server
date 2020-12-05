package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.PersonService;
import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class PersonController<T extends Person, ID, T_DTO extends PersonDTO, T_CREATE_DTO extends PersonCreateDTO> extends BasicController<T, ID, T_DTO, T_CREATE_DTO> {
    private final PersonService<T, ID, T_DTO, T_CREATE_DTO> personService;

    public PersonController(PersonService<T, ID, T_DTO, T_CREATE_DTO> personService) {
        super(personService);
        this.personService = personService;
    }

    @GetMapping(params = {"username"})
    T_DTO byUsername(@PathVariable String username) {
        return personService.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = {"name"})
    List<T_DTO> allByName(@PathVariable String name) {
        return personService.findAllByName(name);
    }
}
