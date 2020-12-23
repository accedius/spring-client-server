package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.WorkResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class WorkHandler extends BasicHandler<WorkDTO, WorkCreateDTO> {
    @Autowired
    private final WorkResource workResource;

    public WorkHandler(WorkResource workResource) {
        super(workResource);
        this.workResource = workResource;
    }

    @Override
    protected void printModel(WorkDTO model) {
        ;
    }

    @Override
    protected boolean checkArgumentsForCreateModel(ApplicationArguments args) {
        return false;
    }

    @Override
    protected WorkCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected String getIdFromArguments(ApplicationArguments args) {
        return null;
    }

    @Override
    protected void printPagedModel(WorkDTO model) {

    }
}
