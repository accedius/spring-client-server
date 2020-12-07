package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.WorkService;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.assembler.WorkModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WorkController.WORK_DOMAIN_ROOT)
public class WorkController extends BasicController<Work, WorkDTO, WorkCreateDTO>{
    public static final String WORK_DOMAIN_ROOT = "/works";
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService, WorkModelAssembler workModelAssembler, PagedResourcesAssembler<Work> pagedResourcesAssembler) {
        super(workService, workModelAssembler, pagedResourcesAssembler);
        this.workService = workService;
    }

    @GetMapping(params = {"title"})
    public List<WorkDTO> allByTitle(@RequestParam String title) {
        if(title == null || title.isBlank());
        return workService.findAllByTitle(title);
    }
}
