package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkService extends BasicService<Work, Integer, WorkDTO, WorkCreateDTO> {
    private final WorkRepository workRepository;
    private final StudentService studentService;
    private final AssessmentService assessmentService;

    @Autowired
    public WorkService(WorkRepository workRepository, @Lazy StudentService studentService, @Lazy AssessmentService assessmentService) {
        super(workRepository);
        this.workRepository = workRepository;
        this.studentService = studentService;
        this.assessmentService = assessmentService;
    }

    public List<WorkDTO> findAllByTitle(String title) {
        List<Work> works = workRepository.findAllByTitle(title);
        return works.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkDTO create(WorkCreateDTO workDTO) throws Exception {
        List<Integer> authorIds = workDTO.getAuthorIds();
        List<Student> authors = getAuthorByIds(authorIds);
        //author(s) should exist in time of the creation => no works without authors can be created, but works will remain after author(s) deletion
        if(authorIds.size() != authors.size())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_CREATE + ServiceConstants.WORK_SERVICE + "some authors of the work not found in db!");

        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_CREATE + ServiceConstants.WORK_SERVICE + "assessment not found in db!");

        Work work = new Work(workDTO.getTitle(), workDTO.getText(), authors, assessment);

        return toDTO(work);
    }

    @Override
    @Transactional
    public WorkDTO update(Integer id, WorkCreateDTO workDTO) throws Exception {
        Optional<Work> optWork = findById(id);
        if(optWork.isEmpty())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.WORK_SERVICE + "work not found in db");

        Work work = optWork.get();
        work.setTitle(workDTO.getTitle());
        work.setText(workDTO.getText());

        List<Integer> authorIds = workDTO.getAuthorIds();
        List<Student> authors = getAuthorByIds(authorIds);
        if(authorIds.size() != authors.size())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.WORK_SERVICE + "some authors of the work not found in db!");
        work.setAuthors(authors);

        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.WORK_SERVICE + "assessment not found in db!");
        work.setAssessment(assessment);

        return toDTO(work);
    }

    private List<Student> getAuthorByIds(List<Integer> authorIds) {
        return studentService.findByIds(authorIds);
    }

    private Assessment getAssessmentById(Integer assessmentId) {
        return assessmentService.findById(assessmentId).orElse(null);
    }

    //TODO find a nicer way to implement - mb adding implements generic interface
    @Override
    protected WorkDTO toDTO(Work work) {
        return new WorkDTO(work);
    }
}
