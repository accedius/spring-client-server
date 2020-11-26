package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public abstract class BasicController<T, ID, T_DTO, T_CREATE_DTO> {
    private final BasicService<T, ID, T_DTO, T_CREATE_DTO> service;

    //TODO check situation with @Qualifier, autowire tells where are two instances of basicService beans: BasicService and PersonService, why?
    @Autowired
    public BasicController(@Qualifier("basicService") BasicService<T, ID, T_DTO, T_CREATE_DTO> service) {
        this.service = service;
    }

    //TODO find a nicer solution with domain varible
    @GetMapping("{domain}/all")
    List<T_DTO> all(@PathVariable String domain) {
        return service.findAll();
    }

    @GetMapping("{domain}/{id}")
    T_DTO byId(@PathVariable String domain, @PathVariable ID id) {
        return service.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{domain}/")
    T_DTO save(@PathVariable String domain, @RequestBody T_CREATE_DTO itemDTO) throws Exception {
        return service.create(itemDTO);
    }

    @PutMapping("{domain}/{id}")
    T_DTO save(@PathVariable String domain, @PathVariable ID id, @RequestBody T_CREATE_DTO itemDTO) throws Exception {
        return service.update(id, itemDTO);
    }

    @DeleteMapping("{domain}/{id}")
    public void delete(@PathVariable String domain, @PathVariable ID id) {
        service.deleteById(id);
    }
}
