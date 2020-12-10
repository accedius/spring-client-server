package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.server.business.controller.BasicController;
import cz.cvut.fit.baklaal1.model.data.entity.ConvertibleToDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.ReadableId;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Collection;

public abstract class ConvertibleModelAssembler<T extends ConvertibleToDTO<T_DTO>, T_DTO extends RepresentationModel<T_DTO> & ReadableId> extends RepresentationModelAssemblerSupport<T, T_DTO> {
    private final Class<? extends BasicController> controllerClass;

    public ConvertibleModelAssembler(Class<? extends BasicController> controllerClass, Class<T_DTO> resourceType) {
        super(controllerClass, resourceType);
        this.controllerClass = controllerClass;
    }

    @Override
    public T_DTO toModel(T entity) {
        T_DTO model = entity.toDTO();
        addLinksToModel(model);
        return model;
    }

    public T_DTO addLinksToModel(T_DTO model) {
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(controllerClass).readById(model.readId())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(controllerClass).readPage(0, 10)).withRel(IanaLinkRelations.COLLECTION));
        return model;
    }

    public <C_T_DTO extends Collection<T_DTO>> C_T_DTO addLinksToModels(C_T_DTO models) {
        models.forEach(this::addLinksToModel);
        return models;
    }
}
