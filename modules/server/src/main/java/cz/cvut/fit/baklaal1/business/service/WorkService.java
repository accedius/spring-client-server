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
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional
public class WorkService extends BasicService<Work, WorkDTO, WorkCreateDTO> {
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

    public Set<WorkDTO> findAllByTitleAsDTO(String title) {
        Set<Work> works = workRepository.findAllByTitle(title);
        Set<WorkDTO> worksAsDTO = toDTO(works);
        return worksAsDTO;
    }

    @Override
    public WorkDTO create(WorkCreateDTO workDTO) throws Exception {
        final String actionCreate = ServiceConstants.ACTION_CREATE;

        //author(s) should exist in time of the creation => no works without authors can be created, but works will remain after author(s) deletion
        Set<Integer> authorIds = workDTO.getAuthorIds();
        if(authorIds.isEmpty())
            throw getServiceException(actionCreate, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Set<Student> authors = getAuthorByIds(authorIds);
        if(authorIds.size() != authors.size())
            throw getServiceException(actionCreate, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        //Assessment should be null on creation, but still better check
        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw getServiceException(actionCreate, ServiceConstants.ASSESSMENT + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Work work = new Work(workDTO.getTitle(), workDTO.getText(), authors, assessment);

        if(exists(work))
            throw getServiceException(actionCreate, ServiceConstants.WORK + ServiceConstants.ALREADY_EXISTS, workDTO);

        Work savedWork = workRepository.save(work);

        return toDTO(savedWork);
    }

    @Override
    public WorkDTO update(Integer id, WorkCreateDTO workDTO) throws Exception {
        final String actionUpdate = ServiceConstants.ACTION_UPDATE;

        Optional<Work> optWork = findById(id);
        if(optWork.isEmpty())
            throw getServiceException(actionUpdate, ServiceConstants.WORK + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Work work = optWork.get();
        work.setTitle(workDTO.getTitle());
        work.setText(workDTO.getText());

        Set<Integer> authorIds = workDTO.getAuthorIds();
        Set<Student> authors = getAuthorByIds(authorIds);
        if(authorIds.size() != authors.size())
            throw getServiceException(actionUpdate, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);
        work.setAuthors(authors);

        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw getServiceException(actionUpdate, ServiceConstants.ASSESSMENT + ServiceConstants.NOT_FOUND_IN_DB, workDTO);
        work.setAssessment(assessment);

        Work savedWork = workRepository.save(work);

        return toDTO(savedWork);
    }

    @Override
    protected boolean exists(final Work item) {
        return !workRepository.findAllByTitleAndAuthorsIn(item.getTitle(), item.getAuthors()).isEmpty();
    }

    @Override
    protected String getServiceName() {
        return ServiceConstants.WORK_SERVICE;
    }

    private Set<Student> getAuthorByIds(Set<Integer> authorIds) {
        return studentService.findAllByIds(authorIds);
    }

    private Assessment getAssessmentById(Integer assessmentId) {
        return assessmentService.findById(assessmentId).orElse(null);
    }

    @Override
    protected WorkDTO toDTO(Work work) {
        return new WorkDTO(work);
    }
}
