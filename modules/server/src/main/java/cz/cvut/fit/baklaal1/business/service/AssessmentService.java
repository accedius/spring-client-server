package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.AssessmentRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AssessmentService(AssessmentRepository assessmentRepository, WorkService workService, TeacherService teacherService) {
        super(assessmentRepository);
        this.assessmentRepository = assessmentRepository;
        this.workService = workService;
        this.teacherService = teacherService;
    }

    //TODO in Java there is no typedef, when what is the best practice to avoid type inconsistency (e.g. int vs long) in var/fields such as Ids?
    public List<AssessmentDTO> findAllByEvaluatorId(int evaluatorId) {
        List<Assessment> assessments = assessmentRepository.findAllByEvaluatorId(evaluatorId);
        return toDTO(assessments);
    }
    
    @Override
    @Transactional
    public AssessmentDTO create(AssessmentCreateDTO assessmentDTO) throws Exception {
        Integer evaluatorId = assessmentDTO.getEvaluatorId();
        Teacher evaluator = getEvaluatorById(evaluatorId);
        if(evaluator == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_CREATE + ServiceConstants.ASSESSMENT_SERVICE + "evaluator of the assessment not found in db!");

        Integer workId = assessmentDTO.getWorkId();
        Work work = getWorkById(workId);
        if(work == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_CREATE + ServiceConstants.ASSESSMENT_SERVICE + "work not found in db!");

        Assessment assessment = new Assessment(assessmentDTO.getGrade(), work, evaluator);

        return toDTO(assessment);
    }

    @Override
    @Transactional
    public AssessmentDTO update(Integer id, AssessmentCreateDTO assessmentDTO) throws Exception {
        Optional<Assessment> optAssessment = findById(id);
        if(optAssessment.isEmpty())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.ASSESSMENT_SERVICE + "assessment not found in db");

        Assessment assessment = optAssessment.get();
        assessment.setGrade(assessmentDTO.getGrade());

        Integer evaluatorId = assessmentDTO.getEvaluatorId();
        Teacher evaluator = getEvaluatorById(evaluatorId);
        if(evaluator == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.ASSESSMENT_SERVICE + "evaluator of the assessment not found in db!");
        assessment.setEvaluator(evaluator);

        Integer workId = assessmentDTO.getWorkId();
        Work work = getWorkById(workId);
        if(work == null)
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.ASSESSMENT_SERVICE + "work not found in db!");
        assessment.setWork(work);

        return toDTO(assessment);
    }

    private Teacher getEvaluatorById(Integer evaluatorId) {
        return teacherService.findById(evaluatorId).orElse(null);
    }

    private Work getWorkById(Integer workId) {
        return workService.findById(workId).orElse(null);
    }

    @Override
    protected AssessmentDTO toDTO(Assessment assessment) {
        return new AssessmentDTO(assessment);
    }
}
