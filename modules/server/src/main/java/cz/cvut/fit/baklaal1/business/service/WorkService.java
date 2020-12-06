package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceException;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionBuilder;
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
import java.util.stream.Collectors;

@Service
@Transactional
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
    public WorkDTO create(WorkCreateDTO workDTO) throws Exception {
        final String actionName = ServiceConstants.ACTION_CREATE;

        //author(s) should exist in time of the creation => no works without authors can be created, but works will remain after author(s) deletion
        Set<Integer> authorIds = workDTO.getAuthorIds();
        if(authorIds.isEmpty())
            throw getServiceException(actionName, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Set<Student> authors = getAuthorByIds(authorIds);
        if(authorIds.size() != authors.size())
            throw getServiceException(actionName, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw getServiceException(actionName, ServiceConstants.ASSESSMENT + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Work work = new Work(workDTO.getTitle(), workDTO.getText(), authors, assessment);
        Work savedWork = workRepository.save(work);

        return toDTO(savedWork);
    }

    @Override
    public WorkDTO update(Integer id, WorkCreateDTO workDTO) throws Exception {
        final String actionName = ServiceConstants.ACTION_UPDATE;

        Optional<Work> optWork = findById(id);
        if(optWork.isEmpty())
            throw getServiceException(actionName, ServiceConstants.WORK + ServiceConstants.NOT_FOUND_IN_DB, workDTO);

        Work work = optWork.get();
        work.setTitle(workDTO.getTitle());
        work.setText(workDTO.getText());

        Set<Integer> authorIds = workDTO.getAuthorIds();
        Set<Student> authors = getAuthorByIds(authorIds);
        if(authorIds.size() != authors.size())
            throw getServiceException(actionName, ServiceConstants.AUTHORS + ServiceConstants.NOT_FOUND_IN_DB, workDTO);
        work.setAuthors(authors);

        Integer assessmentId = workDTO.getAssessmentId();
        Assessment assessment = assessmentId != null ? getAssessmentById(assessmentId) : null;
        if(assessmentId != null && assessment == null)
            throw getServiceException(actionName, ServiceConstants.ASSESSMENT + ServiceConstants.NOT_FOUND_IN_DB, workDTO);
        work.setAssessment(assessment);
        Work savedWork = workRepository.save(work);

        return toDTO(savedWork);
    }

    private Set<Student> getAuthorByIds(Set<Integer> authorIds) {
        return studentService.findByIds(authorIds);
    }

    private Assessment getAssessmentById(Integer assessmentId) {
        return assessmentService.findById(assessmentId).orElse(null);
    }

    private ServiceException getServiceException(String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.WORK_SERVICE).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    @Override
    protected WorkDTO toDTO(Work work) {
        return new WorkDTO(work);
    }
}
