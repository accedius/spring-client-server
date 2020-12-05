package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.StudentRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceException;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionBuilder;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService extends PersonService<Student, Integer, StudentDTO, StudentCreateDTO> {
    private final StudentRepository studentRepository;
    private final WorkService workService;

    @Autowired
    public StudentService(StudentRepository studentRepository, @Lazy WorkService workService) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.workService = workService;
    }

    @Override
    @Transactional
    public StudentDTO create(StudentCreateDTO studentDTO) throws Exception {
        List<Work> works = getRequiredWorkByCreateDTO(studentDTO, ServiceConstants.ACTION_CREATE);

        //TODO check if works right && better than new Student(studentDTO.getUsername(), studentDTO.getName(), studentDTO.getBirthDate(), studentDTO.getAverageGrade(), works);
        Student student = fillStudent(new Student(), studentDTO, works);

        return toDTO(student);
    }

    @Override
    @Transactional
    public StudentDTO update(Integer id, StudentCreateDTO studentDTO) throws Exception {
        Optional<Student> optStudent = findById(id);
        if(optStudent.isEmpty())
            throw getServiceException(ServiceConstants.ACTION_UPDATE, ServiceConstants.STUDENT + ServiceConstants.NOT_FOUND_IN_DB, studentDTO);

        List<Work> works = getRequiredWorkByCreateDTO(studentDTO, ServiceConstants.ACTION_UPDATE);

        Student student = fillStudent(optStudent.get(), studentDTO, works);

        return toDTO(student);
    }

    protected Student fillStudent(Student student, StudentCreateDTO studentDTO, List<Work> works) throws Exception {
        fillPerson(student, studentDTO);
        student.setAverageGrade(studentDTO.getAverageGrade());
        student.setWorks(works);
        return student;
    }

    protected List<Work> getRequiredWorkByCreateDTO(StudentCreateDTO studentDTO, final String ACTION_NAME) throws Exception {
        List<Integer> workIds = studentDTO.getWorkIds();
        List<Work> works = getWorkByIds(workIds);
        if(workIds.size() != works.size())
            throw getServiceException(ACTION_NAME, ServiceConstants.WORKS + ServiceConstants.NOT_FOUND_IN_DB, studentDTO);
        return works;
    }

    private List<Work> getWorkByIds(List<Integer> workIds) {
        return workService.findByIds(workIds);
    }

    private ServiceException getServiceException(String duringActionName, String cause) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.STUDENT_SERVICE).onAction(duringActionName).causedBy(cause);
        return builder.build();
    }

    private ServiceException getServiceException(String duringActionName, String cause, Object relatedObject) {
        ServiceExceptionBuilder builder = new ServiceExceptionBuilder();
        builder.exception().inService(ServiceConstants.STUDENT_SERVICE).onAction(duringActionName).causedBy(cause).relatedToObject(relatedObject);
        return builder.build();
    }

    @Override
    protected StudentDTO toDTO(Student student) {
        return new StudentDTO(student);
    }
}
