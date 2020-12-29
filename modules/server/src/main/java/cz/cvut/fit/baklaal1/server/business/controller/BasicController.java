package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.model.data.entity.dto.BasicDTO;
import cz.cvut.fit.baklaal1.server.business.service.BasicService;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionInBusinessLogic;
import cz.cvut.fit.baklaal1.entity.ConvertibleToDTO;
import cz.cvut.fit.baklaal1.server.data.entity.dto.assembler.ConvertibleModelAssembler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicController<T extends ConvertibleToDTO<T_DTO>, T_DTO extends BasicDTO<T_DTO>, T_CREATE_DTO> {
    private final BasicService<T, T_DTO, T_CREATE_DTO> service;
    protected final ConvertibleModelAssembler<T, T_DTO> modelAssembler;
    protected final PagedResourcesAssembler<T> pagedResourcesAssembler;

    public BasicController(BasicService<T, T_DTO, T_CREATE_DTO> service, ConvertibleModelAssembler<T, T_DTO> modelAssembler, PagedResourcesAssembler<T> pagedResourcesAssembler) {
        this.service = service;
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    public List<T_DTO> readAll() {
        List<T> all = service.findAll();
        List<T_DTO> allAsDTO = all.stream().map(modelAssembler::toModel).collect(Collectors.toList());
        return allAsDTO;
    }

    @GetMapping("/{id}")
    public T_DTO readById(@PathVariable int id) {
        T foundItem = service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        T_DTO foundAsDTO = modelAssembler.toModel(foundItem);
        return foundAsDTO;
    }

    @GetMapping
    public PagedModel<T_DTO> readPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return pagedResourcesAssembler.toModel(service.pageAll(PageRequest.of(page, size)), modelAssembler);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody T_CREATE_DTO itemDTO) {
        try {
            T_DTO createdDTO = service.create(itemDTO);
            return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).readById(createdDTO.getId())).toUri()).build();
        } catch (ServiceExceptionInBusinessLogic e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public T_DTO update(@PathVariable int id, @RequestBody T_CREATE_DTO itemDTO) {
        try {
            T_DTO updated = service.update(id, itemDTO);
            modelAssembler.addLinksToModel(updated);
            return updated;
        } catch (ServiceExceptionInBusinessLogic e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
