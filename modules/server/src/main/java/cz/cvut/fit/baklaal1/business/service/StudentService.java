package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.StudentRepository;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceConstants;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService extends PersonService<Student, Integer, StudentDTO, StudentCreateDTO> {
    private final StudentRepository studentRepository;
    private final WorkService workService;

    @Autowired
    public StudentService(StudentRepository studentRepository, WorkService workService) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.workService = workService;
    }

    @Override
    @Transactional
    public StudentDTO create(StudentCreateDTO studentDTO) throws Exception {
        List<Integer> workIds = studentDTO.getWorkIds();
        List<Work> works = getRequiredWorkByIds(workIds, ServiceConstants.ON_CREATE);

        //TODO check if works right && better than new Student(studentDTO.getUsername(), studentDTO.getName(), studentDTO.getBirthDate(), studentDTO.getAverageGrade(), works);
        Student student = fillStudent(new Student(), studentDTO, works);

        return toDTO(student);
    }

    @Override
    @Transactional
    public StudentDTO update(Integer id, StudentCreateDTO studentDTO) throws Exception {
        Optional<Student> optStudent = findById(id);
        if(optStudent.isEmpty())
            throw new Exception(ServiceConstants.EXCEPTION + ServiceConstants.ON_UPDATE + ServiceConstants.STUDENT_SERVICE + "student not found in db");

        List<Integer> workIds = studentDTO.getWorkIds();
        List<Work> works = getRequiredWorkByIds(workIds, ServiceConstants.ON_UPDATE);

        Student student = fillStudent(optStudent.get(), studentDTO, works);

        return toDTO(student);
    }

    protected Student fillStudent(Student student, StudentCreateDTO studentDTO, List<Work> works) throws Exception {
        fillPerson(student, studentDTO);
        student.setAverageGrade(studentDTO.getAverageGrade());
        student.setWorks(works);
        return student;
    }

    protected List<Work> getRequiredWorkByIds(List<Integer> workIds, final String ON_ACTION) throws Exception{
        List<Work> works = getWorkByIds(workIds);
        if(workIds.size() != works.size())
            throw new Exception(ServiceConstants.EXCEPTION + ON_ACTION + ServiceConstants.STUDENT_SERVICE + "some works of the student not found in db!");
        return works;
    }

    private List<Work> getWorkByIds(List<Integer> workIds) {
        return workService.findByIds(workIds);
    }

    @Override
    protected StudentDTO toDTO(Student student) {
        return new StudentDTO(student);
    }
}
