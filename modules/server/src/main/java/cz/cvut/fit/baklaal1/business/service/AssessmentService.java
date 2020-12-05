package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.AssessmentRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceException;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionBuilder;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService extends BasicService<Assessment, Integer, AssessmentDTO, AssessmentCreateDTO> {
    private final AssessmentRepository assessmentRepository;
    private final WorkService workService;
    private final TeacherService teacherService;

    @Autowired
    public AssessmentService(AssessmentRepository assessmentRepository, @Lazy WorkService workService, @Lazy TeacherService teacherService) {
        super(assessmentRepository);
        this.assessmentRepository = assessmentRepository;
        this.workService = workService;
        this.teacherService = teacherService;
    }

    public List<AssessmentDTO> findAllByEvaluatorId(int evaluatorId) {
        List<Assessment> assessments = assessmentRepository.findAllByEvaluatorId(evaluatorId);
        return toDTO(assessments);
    }
    
    @Override
    @Transactional
    public AssessmentDTO create(AssessmentCreateDTO assessmentDTO) throws Exception {
        final String actionName = ServiceConstants.ACTION_CREATE;

        Integer evaluatorId = assessmentDTO.getEvaluatorId();
        Teacher evaluator = getEvaluatorById(evaluatorId);
        if(evaluator == null)
            throw getServiceException(actionName, ServiceConstants.EVALUATOR + ServiceConstants.NOT_FOUND_IN_DB, assessmentDTO);

        Integer workId = assessmentDTO.getWorkId();
        Work work = getWorkById(workId);
        if(work == null)
            throw getServiceException(actionName, ServiceConstants.WORK + ServiceConstants.NOT_FOUND_IN_DB, assessmentDTO);

        Assessment assessment = new Assessment(assessmentDTO.getGrade(), work, evaluator);

        return toDTO(assessment);
    }

    @Override
    @Transactional
    public AssessmentDTO update(Integer id, AssessmentCreateDTO assessmentDTO) throws Exception {
        final String actionName = ServiceConstants.ACTION_UPDATE;

        Optional<Assessment> optAssessment = findById(id);
        if(optAssessment.isEmpty())
            throw getServiceException(actionName, ServiceConstants.ASSESSMENT + ServiceConstants.NOT_FOUND_IN_DB, assessmentDTO);

        Assessment assessment = optAssessment.get();
        assessment.setGrade(assessmentDTO.getGrade());

        Integer evaluatorId = assessmentDTO.getEvaluatorId();
        Teacher evaluator = getEvaluatorById(evaluatorId);
        if(evaluator == null)
            throw getServiceException(actionName, ServiceConstants.EVALUATOR + ServiceConstants.NOT_FOUND_IN_DB, assessmentDTO);
        assessment.setEvaluator(evaluator);

        Integer workId = assessmentDTO.getWorkId();
        Work work = getWorkById(workId);
        if(work == null)
            throw getServiceException(actionName, ServiceConstants.WORK + ServiceConstants.NOT_FOUND_IN_DB, assessmentDTO);
        assessment.setWork(work);

        return toDTO(assessment);
    }

    private Teacher getEvaluatorById(Integer evaluatorId) {
        return teacherService.findById(evaluatorId).orElse(null);
    }

    private Work getWorkById(Integer workId) {
        return workService.findById(workId).orElse(null);
    }

    private ServiceException getServiceException(String duringActionName, String cause) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.ASSESSMENT_SERVICE).onAction(duringActionName).causedBy(cause);
        return builder.build();
    }

    private ServiceException getServiceException(String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.ASSESSMENT_SERVICE).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    @Override
    protected AssessmentDTO toDTO(Assessment assessment) {
        return new AssessmentDTO(assessment);
    }
}
