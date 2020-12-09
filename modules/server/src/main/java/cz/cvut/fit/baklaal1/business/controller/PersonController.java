package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.PersonService;
import cz.cvut.fit.baklaal1.data.entity.ConvertibleToDTO;
import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.ConvertibleModelAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class PersonController<T extends Person & ConvertibleToDTO<T_DTO>, T_DTO extends PersonDTO<T_DTO>, T_CREATE_DTO extends PersonCreateDTO> extends BasicController<T, T_DTO, T_CREATE_DTO> {
    private final PersonService<T, T_DTO, T_CREATE_DTO> personService;

    public PersonController(PersonService<T, T_DTO, T_CREATE_DTO> personService, ConvertibleModelAssembler<T, T_DTO> itemModelAssembler, PagedResourcesAssembler<T> pagedResourcesAssembler) {
        super(personService, itemModelAssembler, pagedResourcesAssembler);
        this.personService = personService;
    }

    @GetMapping(params = {"username"})
    public T_DTO readByUsername(@RequestParam String username) {
        return personService.findByUsernameAsDTO(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = {"name"})
    public List<T_DTO> readAllByName(@RequestParam String name) {
        return personService.findAllByNameAsDTO(name);
    }
}
