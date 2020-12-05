package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.TeacherRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceException;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionBuilder;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService extends PersonService<Teacher, Integer, TeacherDTO, TeacherCreateDTO> {
    private final TeacherRepository teacherRepository;
    private final AssessmentService assessmentService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, @Lazy AssessmentService assessmentService) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.assessmentService = assessmentService;
    }

    @Override
    @Transactional
    public TeacherDTO create(TeacherCreateDTO teacherDTO) throws Exception {
        List<Assessment> assessments = getRequiredAssessmentByCreateDTO(teacherDTO, ServiceConstants.ACTION_CREATE);

        Teacher teacher = fillTeacher(new Teacher(), teacherDTO, assessments);

        return toDTO(teacher);
    }

    @Override
    @Transactional
    public TeacherDTO update(Integer id, TeacherCreateDTO teacherDTO) throws Exception {
        Optional<Teacher> optTeacher = findById(id);
        if(optTeacher.isEmpty())
            throw getServiceException(ServiceConstants.ACTION_UPDATE, ServiceConstants.TEACHER + ServiceConstants.NOT_FOUND_IN_DB, teacherDTO);

        List<Assessment> assessments = getRequiredAssessmentByCreateDTO(teacherDTO, ServiceConstants.ACTION_UPDATE);

        Teacher teacher = fillTeacher(optTeacher.get(), teacherDTO, assessments);

        return toDTO(teacher);
    }

    protected Teacher fillTeacher(Teacher teacher, TeacherCreateDTO teacherDTO, List<Assessment> assessments) throws Exception {
        fillPerson(teacher, teacherDTO);
        teacher.setWage(teacherDTO.getWage());
        teacher.setAssessments(assessments);
        return teacher;
    }

    protected List<Assessment> getRequiredAssessmentByCreateDTO(TeacherCreateDTO teacherDTO, final String ACTION_NAME) throws Exception {
        List<Integer> assessmentIds = teacherDTO.getAssessmentIds();
        List<Assessment> assessments = getAssessmentByIds(assessmentIds);
        if(assessmentIds.size() != assessments.size())
            throw getServiceException(ACTION_NAME, ServiceConstants.ASSESSMENTS + ServiceConstants.NOT_FOUND_IN_DB, teacherDTO);
        return assessments;
    }

    private List<Assessment> getAssessmentByIds(List<Integer> assessmentIds) {
        return assessmentService.findByIds(assessmentIds);
    }

    private ServiceException getServiceException(String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.TEACHER_SERVICE).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    @Override
    protected TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher);
    }
}
