package cz.cvut.fit.baklaal1.data.entity.dto.assembler;

import cz.cvut.fit.baklaal1.business.controller.WorkController;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkModelAssembler extends ConvertibleModelAssembler<Work, WorkDTO> {
    public WorkModelAssembler() {
        super(WorkController.class, WorkDTO.class);
    }
}
