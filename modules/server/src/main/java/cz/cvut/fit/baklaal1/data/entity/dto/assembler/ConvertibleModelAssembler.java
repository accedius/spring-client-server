package cz.cvut.fit.baklaal1.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.business.controller.BasicController;
import cz.cvut.fit.baklaal1.data.entity.ConvertibleToDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.ReadableId;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

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
        return model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(controllerClass).readById(model.readId())).withSelfRel());
    }
}
