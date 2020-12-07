package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.BasicService;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionInBusinessLogic;
import cz.cvut.fit.baklaal1.data.entity.ConvertibleToDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.ReadableId;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.ConvertibleModelAssembler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class BasicController<T extends ConvertibleToDTO<T_DTO>, T_DTO extends RepresentationModel<T_DTO> & ReadableId, T_CREATE_DTO> {
    private final BasicService<T, T_DTO, T_CREATE_DTO> service;
    private final ConvertibleModelAssembler<T, T_DTO> modelAssembler;
    private final PagedResourcesAssembler<T> pagedResourcesAssembler;

    public BasicController(BasicService<T, T_DTO, T_CREATE_DTO> service, ConvertibleModelAssembler<T, T_DTO> modelAssembler, PagedResourcesAssembler<T> pagedResourcesAssembler) {
        this.service = service;
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    public List<T_DTO> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public T_DTO byId(@PathVariable int id) {
        T_DTO foundAsDTO = service.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        foundAsDTO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).readPage(0, 10)).withRel(IanaLinkRelations.COLLECTION));
        return foundAsDTO;
    }

    @GetMapping
    public PagedModel<T_DTO> readPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return pagedResourcesAssembler.toModel(service.pageAll(PageRequest.of(page, size)), modelAssembler);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody T_CREATE_DTO itemDTO) {
        try {
            T_DTO createdDTO = service.create(itemDTO);
            return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).byId(createdDTO.readId())).toUri()).build();
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
            return service.update(id, itemDTO);
        } catch (ServiceExceptionInBusinessLogic e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        service.deleteById(id);
    }
}
