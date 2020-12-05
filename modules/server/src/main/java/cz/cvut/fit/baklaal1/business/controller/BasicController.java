package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.BasicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class BasicController<T, ID, T_DTO, T_CREATE_DTO> {
    private final BasicService<T, ID, T_DTO, T_CREATE_DTO> service;

    public BasicController( BasicService<T, ID, T_DTO, T_CREATE_DTO> service) {
        this.service = service;
    }

    @GetMapping("/all")
    List<T_DTO> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    T_DTO byId(@PathVariable ID id) {
        return service.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    T_DTO save(@RequestBody T_CREATE_DTO itemDTO) throws Exception {
        return service.create(itemDTO);
    }

    @PutMapping("/{id}")
    T_DTO save(@PathVariable ID id, @RequestBody T_CREATE_DTO itemDTO) throws Exception {
        return service.update(id, itemDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        service.deleteById(id);
    }
}
