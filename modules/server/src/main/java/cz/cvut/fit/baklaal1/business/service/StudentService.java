package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.StudentRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class StudentService extends PersonService<Student, StudentDTO, StudentCreateDTO> {
    private final StudentRepository studentRepository;
    private final WorkService workService;

    private static final String ACTION_JOIN_WORK = "join work";

    @Autowired
    public StudentService(StudentRepository studentRepository, @Lazy WorkService workService) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.workService = workService;
    }

    @Override
    public StudentDTO create(StudentCreateDTO studentDTO) throws Exception {
        final String actionCreate = ServiceConstants.ACTION_CREATE;

        Set<Work> works = getRequiredWorkByCreateDTO(studentDTO, ServiceConstants.ACTION_CREATE);

        //TODO check if works right && better than new Student(studentDTO.getUsername(), studentDTO.getName(), studentDTO.getBirthDate(), studentDTO.getAverageGrade(), works);
        Student student = fillStudent(new Student(), studentDTO, works);

        if(exists(student))
            throw getServiceException(actionCreate, ServiceConstants.WORK + ServiceConstants.ALREADY_EXISTS, studentDTO);

        Student savedStudent = studentRepository.save(student);

        return toDTO(savedStudent);
    }

    @Override
    public StudentDTO update(Integer id, StudentCreateDTO studentDTO) throws Exception {
        final String actionUpdate = ServiceConstants.ACTION_UPDATE;

        Optional<Student> optStudent = findById(id);
        if(optStudent.isEmpty())
            throw getServiceException(actionUpdate, ServiceConstants.STUDENT + ServiceConstants.NOT_FOUND_IN_DB, studentDTO);

        Set<Work> works = getRequiredWorkByCreateDTO(studentDTO, ServiceConstants.ACTION_UPDATE);

        Student student = fillStudent(optStudent.get(), studentDTO, works);
        Student savedStudent = studentRepository.save(student);

        return toDTO(savedStudent);
    }

    public void joinWork(int studentId, int workId) throws Exception {
        final String actionName = ACTION_JOIN_WORK;

        Optional<Work> optionalWork = workService.findById(workId);
        if(optionalWork.isEmpty())
            throw getServiceException(actionName, ServiceConstants.WORK + ServiceConstants.NOT_FOUND_IN_DB, workId);

        Optional<Student> optionalStudent = this.findById(studentId);
        if(optionalStudent.isEmpty())
            throw getServiceException(actionName, ServiceConstants.STUDENT + ServiceConstants.NOT_FOUND_IN_DB, studentId);

        Work work = optionalWork.get();
        Student student = optionalStudent.get();

        if(!work.getAuthors().contains(student)) {
            work.getAuthors().add(student);
            workService.update(workId, work.toCreateDTO());
        }
    }

    @Override
    protected String getServiceName() {
        return ServiceConstants.STUDENT_SERVICE;
    }

    protected Student fillStudent(Student student, StudentCreateDTO studentDTO, Set<Work> works) throws Exception {
        fillPerson(student, studentDTO);
        student.setAverageGrade(studentDTO.getAverageGrade());
        student.setWorks(works);
        return student;
    }

    protected Set<Work> getRequiredWorkByCreateDTO(StudentCreateDTO studentDTO, final String ACTION_NAME) throws Exception {
        Set<Integer> workIds = studentDTO.getWorkIds();
        Set<Work> works = getWorkByIds(workIds);
        if(workIds.size() != works.size())
            throw getServiceException(ACTION_NAME, ServiceConstants.WORKS + ServiceConstants.NOT_FOUND_IN_DB, studentDTO);
        return works;
    }

    private Set<Work> getWorkByIds(Set<Integer> workIds) {
        return workService.findByIds(workIds);
    }

    @Override
    protected StudentDTO toDTO(Student student) {
        return new StudentDTO(student);
    }
}
