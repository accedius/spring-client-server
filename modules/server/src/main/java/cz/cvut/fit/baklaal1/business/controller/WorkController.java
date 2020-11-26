package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.controller.helper.ControllerConstants;
import cz.cvut.fit.baklaal1.business.service.WorkService;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkController extends BasicController<Work, Integer, WorkDTO, WorkCreateDTO>{
    private final WorkService workService;
    private static final String domainRoot = ControllerConstants.WORK_DOMAIN_ROOT;

    @Autowired
    public WorkController(WorkService workService) {
        super(workService);
        this.workService = workService;
    }

    List<WorkDTO> all() {
        return super.all(domainRoot);
    }

    WorkDTO byId(int id) {
        return super.byId(domainRoot, id);
    }

    WorkDTO save(WorkCreateDTO workDTO) throws Exception {
        return super.save(domainRoot, workDTO);
    }

    WorkDTO save(int id, WorkCreateDTO book) throws Exception {
        return super.save(domainRoot, id, book);
    }

    @GetMapping(value = domainRoot, params = {"title"})
    List<WorkDTO> allByTitle(@RequestParam String title) {
        return workService.findAllByTitle(title);
    }

    /*@GetMapping("/work/all")
    List<WorkDTO> all() {
        return workService.findAll();
    }

    @GetMapping("/work/{id}")
    WorkDTO byId(@PathVariable int id) {
        return workService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/work")
    WorkDTO save(@RequestBody WorkCreateDTO work) throws Exception {
        return workService.create(work);
    }

    @PostMapping(domainRoot)
    WorkDTO save(@RequestBody WorkCreateDTO workDTO) throws Exception {
        return workService.create(workDTO);
    }

    @PutMapping(domainRoot + "/{id}")
    WorkDTO save(@PathVariable int id, @RequestBody WorkCreateDTO book) throws Exception {
        return workService.update(id, book);
    }*/
}
