package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.PersonRepository;
import cz.cvut.fit.baklaal1.business.repository.TeacherRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Teacher;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService extends PersonService<Teacher, Integer, TeacherDTO, TeacherCreateDTO> {
    private final TeacherRepository teacherRepository;
    private final AssessmentService assessmentService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, AssessmentService assessmentService) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.assessmentService = assessmentService;
    }

    @Override
    @Transactional
    public TeacherDTO create(TeacherCreateDTO teacherDTO) throws Exception {
        List<Integer> assessmentIds = teacherDTO.getAssessmentIds();
        List<Assessment> assessments = getRequiredAssessmentByIds(assessmentIds, ServiceConstants.ON_CREATE);

        Teacher teacher = fillTeacher(new Teacher(), teacherDTO, assessments);

        return toDTO(teacher);
    }

    @Override
    @Transactional
    public TeacherDTO update(Integer id, TeacherCreateDTO teacherDTO) throws Exception {
        Optional<Teacher> optTeacher = findById(id);
        if(optTeacher.isEmpty())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.TEACHER_SERVICE + "teacher not found in db");

        List<Integer> assessmentIds = teacherDTO.getAssessmentIds();
        List<Assessment> assessments = getRequiredAssessmentByIds(assessmentIds, ServiceConstants.ON_UPDATE);

        Teacher teacher = fillTeacher(optTeacher.get(), teacherDTO, assessments);

        return toDTO(teacher);
    }

    protected Teacher fillTeacher(Teacher teacher, TeacherCreateDTO teacherDTO, List<Assessment> assessments) throws Exception {
        fillPerson(teacher, teacherDTO);
        teacher.setWage(teacherDTO.getWage());
        teacher.setAssessments(assessments);
        return teacher;
    }

    protected List<Assessment> getRequiredAssessmentByIds(List<Integer> assessmentIds, final String ON_ACTION) throws Exception{
        List<Assessment> assessments = getAssessmentByIds(assessmentIds);
        if(assessmentIds.size() != assessments.size())
            throw new Exception(ServiceConstants.EXCEPTION + ON_ACTION + ServiceConstants.TEACHER_SERVICE + "some assessments of the teacher not found in db!");
        return assessments;
    }

    private List<Assessment> getAssessmentByIds(List<Integer> assessmentIds) {
        return assessmentService.findByIds(assessmentIds);
    }

    @Override
    protected TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher);
    }
}
