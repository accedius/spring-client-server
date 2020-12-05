package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.WorkService;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WorkController.WORK_DOMAIN_ROOT)
public class WorkController extends BasicController<Work, Integer, WorkDTO, WorkCreateDTO>{
    public static final String WORK_DOMAIN_ROOT = "/work";
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        super(workService);
        this.workService = workService;
    }

    List<WorkDTO> all() {
        return super.all();
    }

    WorkDTO byId(int id) {
        return super.byId(id);
    }

    WorkDTO save(WorkCreateDTO workDTO) throws Exception {
        return super.save(workDTO);
    }

    WorkDTO save(int id, WorkCreateDTO book) throws Exception {
        return super.save(id, book);
    }

    @GetMapping(params = {"title"})
    List<WorkDTO> allByTitle(@RequestParam String title) {
        return workService.findAllByTitle(title);
    }
}
