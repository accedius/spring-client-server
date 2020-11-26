package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.controller.helper.ControllerConstants;
import cz.cvut.fit.baklaal1.business.service.BasicService;
import cz.cvut.fit.baklaal1.business.service.PersonService;
import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public abstract class PersonController<T extends Person, ID, T_DTO extends PersonDTO, T_CREATE_DTO extends PersonCreateDTO> extends BasicController<T, ID, T_DTO, T_CREATE_DTO> {
    private final PersonService<T, ID, T_DTO, T_CREATE_DTO> personService;
    protected static final String domainRoot = ControllerConstants.PERSON_DOMAIN_ROOT;

    @Autowired
    public PersonController(PersonService<T, ID, T_DTO, T_CREATE_DTO> personService) {
        super(personService);
        this.personService = personService;
    }
    
    List<T_DTO> all() {
        return super.all(domainRoot);
    }

    T_DTO byId(ID id) {
        return super.byId(domainRoot, id);
    }

    T_DTO save(T_CREATE_DTO personDTO) throws Exception {
        return super.save(domainRoot, personDTO);
    }

    T_DTO save(ID id, T_CREATE_DTO personDTO) throws Exception {
        return super.save(domainRoot, id, personDTO);
    }

    @GetMapping(value = domainRoot, params = {"username"})
    T_DTO byUsername(@PathVariable String username) {
        return personService.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = domainRoot, params = {"name"})
    List<T_DTO> allByName(@PathVariable String name) {
        return personService.findAllByName(name);
    }
}
