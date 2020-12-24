package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.server.business.repository.TeacherRepository;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Teacher;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TeacherService extends PersonService<Teacher, TeacherDTO, TeacherCreateDTO> {
    private final TeacherRepository teacherRepository;
    private final AssessmentService assessmentService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, @Lazy AssessmentService assessmentService) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.assessmentService = assessmentService;
    }

    @Override
    public TeacherDTO create(TeacherCreateDTO teacherDTO) throws Exception {
        final String actionCreate = ServiceConstants.ACTION_CREATE;

        Set<Assessment> assessments = getRequiredAssessmentByCreateDTO(teacherDTO, ServiceConstants.ACTION_CREATE);

        Teacher teacher = fillTeacher(new Teacher(), teacherDTO, assessments);

        if(exists(teacher))
            throw getServiceException(actionCreate, ServiceConstants.TEACHER + ServiceConstants.ALREADY_EXISTS, teacherDTO);

        Teacher savedTeacher = teacherRepository.save(teacher);

        return toDTO(savedTeacher);
    }

    @Override
    public TeacherDTO update(Integer id, TeacherCreateDTO teacherDTO) throws Exception {
        final String actionUpdate = ServiceConstants.ACTION_UPDATE;

        Optional<Teacher> optTeacher = findById(id);
        if(optTeacher.isEmpty())
            throw getServiceException(actionUpdate, ServiceConstants.TEACHER + ServiceConstants.NOT_FOUND_IN_DB, teacherDTO);

        Set<Assessment> assessments = getRequiredAssessmentByCreateDTO(teacherDTO, ServiceConstants.ACTION_UPDATE);

        Teacher teacher = fillTeacher(optTeacher.get(), teacherDTO, assessments);
        Teacher savedTeacher = teacherRepository.save(teacher);

        return toDTO(savedTeacher);
    }

    @Override
    protected String getServiceName() {
        return ServiceConstants.TEACHER_SERVICE;
    }

    protected Teacher fillTeacher(Teacher teacher, TeacherCreateDTO teacherDTO, Set<Assessment> assessments) throws Exception {
        fillPerson(teacher, teacherDTO);
        teacher.setWage(teacherDTO.getWage());
        teacher.setAssessments(assessments);
        return teacher;
    }

    protected Set<Assessment> getRequiredAssessmentByCreateDTO(TeacherCreateDTO teacherDTO, final String ACTION_NAME) throws Exception {
        Set<Integer> assessmentIds = teacherDTO.getAssessmentIds();
        Set<Assessment> assessments = getAssessmentByIds(assessmentIds);
        if(assessmentIds.size() != assessments.size())
            throw getServiceException(ACTION_NAME, ServiceConstants.ASSESSMENTS + ServiceConstants.NOT_FOUND_IN_DB, teacherDTO);
        return assessments;
    }

    private Set<Assessment> getAssessmentByIds(Set<Integer> assessmentIds) {
        return assessmentService.findAllByIds(assessmentIds);
    }

    @Override
    protected TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher);
    }
}
