package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.server.business.service.WorkService;
import cz.cvut.fit.baklaal1.model.data.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.server.data.entity.dto.assembler.WorkModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(WorkController.WORK_DOMAIN_ROOT)
public class WorkController extends BasicController<Work, WorkDTO, WorkCreateDTO>{
    public static final String WORK_DOMAIN_ROOT = "/works";
    public static final String READ_ALL_BY_TITLE = "/all-by-title";
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService, WorkModelAssembler workModelAssembler, PagedResourcesAssembler<Work> pagedResourcesAssembler) {
        super(workService, workModelAssembler, pagedResourcesAssembler);
        this.workService = workService;
    }

    @RequestMapping(value = READ_ALL_BY_TITLE, method = RequestMethod.GET)
    public Set<WorkDTO> readAllByTitle(@RequestParam String title) {
        Set<WorkDTO> worksAsDTO = workService.findAllByTitleAsDTO(title);
        modelAssembler.addLinksToModels(worksAsDTO);
        return worksAsDTO;
    }
}
