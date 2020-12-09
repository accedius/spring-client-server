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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicController<T extends ConvertibleToDTO<T_DTO>, T_DTO extends RepresentationModel<T_DTO> & ReadableId, T_CREATE_DTO> {
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
        /*List<T_DTO> allAsDTO = new ArrayList<>();
        for(T item : all) {
            T_DTO itemAsDTO = modelAssembler.toModel(item);
            allAsDTO.add(itemAsDTO);
        }*/
        List<T_DTO> allAsDTO = all.stream().map(modelAssembler::toModel).collect(Collectors.toList());
        return allAsDTO;
    }

    @GetMapping("/{id}")
    public T_DTO readById(@PathVariable int id) {
        T foundItem = service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        T_DTO foundAsDTO = modelAssembler.toModel(foundItem);
        //foundAsDTO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).readPage(0, 10)).withRel(IanaLinkRelations.COLLECTION));
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
            return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).readById(createdDTO.readId())).toUri()).build();
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
        service.delete(id);
    }
}
