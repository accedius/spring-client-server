package cz.cvut.fit.baklaal1.server.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.server.business.controller.WorkController;
import org.springframework.stereotype.Component;

@Component
public class WorkModelAssembler extends ConvertibleModelAssembler<Work, WorkDTO> {
    public WorkModelAssembler() {
        super(WorkController.class, WorkDTO.class);
    }
}
