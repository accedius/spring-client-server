package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import cz.cvut.fit.baklaal1.server.business.repository.StudentRepository;
import cz.cvut.fit.baklaal1.server.suite.StudentTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
//TODO maybe should just use one DataSource for all the Test classes, since Spring creates HikariDataSource pool for each Test class in runtime, causing opening and closing same database Connection for each Test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("StudentService Test")
class StudentServiceTest extends StudentTestSuite {
    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private WorkService workServiceMock;

    @Test
    public void findAll() {
        final int allStudentsCnt = 10;
        List<Student> allStudentsToReturn = fillStudentCollection(new ArrayList<>(), allStudentsCnt);
        List<Student> allStudentsToReceive = fillStudentCollection(new ArrayList<>(), allStudentsCnt);

        BDDMockito.given(studentRepositoryMock.findAll()).willReturn(allStudentsToReturn);

        List<Student> receivedAllStudents = studentService.findAll();

        assertEquals(allStudentsToReceive, receivedAllStudents);

        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final int allStudentsCnt = 10;
        List<Student> allStudentsToReturn = fillStudentCollection(new ArrayList<>(), allStudentsCnt);
        List<StudentDTO> allStudentsToReceiveAsDTO = fillStudentDTOCollection(new ArrayList<>(), allStudentsCnt);

        BDDMockito.given(studentRepositoryMock.findAll()).willReturn(allStudentsToReturn);

        List<StudentDTO> receivedAllStudentsAsDTO = studentService.findAllAsDTO();

        assertEquals(allStudentsToReceiveAsDTO, receivedAllStudentsAsDTO);

        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void pageAll() {
        final int allStudentsCnt = 10;
        final int page = 1;
        final int pageSize = 3;
        List<Student> allStudentsToReturn = fillStudentCollection(new ArrayList<>(), allStudentsCnt);
        List<Student> allStudentsToReceive = fillStudentCollection(new ArrayList<>(), allStudentsCnt);

        final Pageable pageableRequested = PageRequest.of(page, pageSize);
        final Page<Student> pageExpected = new PageImpl<>(allStudentsToReceive, pageableRequested, allStudentsCnt);
        final Page<Student> pageToReturn = new PageImpl<>(allStudentsToReturn, pageableRequested, allStudentsCnt);

        BDDMockito.given(studentRepositoryMock.findAll(pageableRequested)).willReturn(pageToReturn);

        final Page<Student> pageReceived = studentService.pageAll(pageableRequested);

        assertEquals(pageExpected, pageReceived);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAll(pageableArgumentCaptor.capture());
        assertEquals(pageableRequested, pageableArgumentCaptor.getValue());
    }

    @Test
    public void findAllByIds() {
        final int allStudentsCnt = 5;
        List<Student> allStudentsToReturn = fillStudentCollection(new ArrayList<>(), allStudentsCnt);
        Set<Student> allStudentsToReceive = fillStudentCollection(new TreeSet<>(), allStudentsCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allStudentsCnt);

        BDDMockito.given(studentRepositoryMock.findAllById(allWantedIds)).willReturn(allStudentsToReturn);

        Set<Student> receivedStudents = studentService.findAllByIds(new TreeSet<>(allWantedIds));
        assertEquals(allStudentsToReceive, receivedStudents);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findAllByIdsAsDTO() {
        final int allStudentsCnt = 5;
        List<Student> allStudentsToReturn = fillStudentCollection(new ArrayList<>(), allStudentsCnt);
        Set<StudentDTO> allStudentsToReceive = fillStudentDTOCollection(new TreeSet<>(), allStudentsCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allStudentsCnt);

        BDDMockito.given(studentRepositoryMock.findAllById(allWantedIds)).willReturn(allStudentsToReturn);

        Set<StudentDTO> receivedStudents = studentService.findAllByIdsAsDTO(new TreeSet<>(allWantedIds));
        assertEquals(allStudentsToReceive, receivedStudents);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findById() {
        final int studentId = 10;
        Student studentToReturn = generateStudent(studentId);
        Student studentToReceive = generateStudent(studentId);

        BDDMockito.given(studentRepositoryMock.findById(studentId)).willReturn(Optional.of(studentToReturn));

        Student received = studentService.findById(studentId).orElseThrow();
        assertEquals(studentToReceive, received);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(studentId, argumentCaptor.getValue());
    }

    @Test
    public void findByIdAsDTO() {
        final int studentId = 10;
        Student studentToReturn = generateStudent(studentId);
        StudentDTO studentDTOToReceive = generateStudentDTO(studentId);

        BDDMockito.given(studentRepositoryMock.findById(studentId)).willReturn(Optional.of(studentToReturn));

        StudentDTO receivedDTO = studentService.findByIdAsDTO(studentId).orElseThrow();
        assertEquals(studentDTOToReceive, receivedDTO);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(studentId, argumentCaptor.getValue());
    }

    @Test
    public void delete() {
        final int studentId = 10;

        BDDMockito.doNothing().when(studentRepositoryMock).deleteById(studentId);

        studentService.delete(studentId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        assertEquals(studentId, idCaptor.getValue());
    }

    @Test
    public void create() throws Exception {
        final int studentId = 2;
        Student studentToReturn = generateStudent(studentId);
        StudentCreateDTO studentCreateDTO = generateStudentCreateDTO(studentId);
        StudentDTO studentDTOToReceive = generateStudentDTO(studentId);

        BDDMockito.given(studentRepositoryMock.save(any(Student.class))).willReturn(studentToReturn);
        BDDMockito.given(studentRepositoryMock.findByUsername(studentCreateDTO.getUsername())).willReturn(Optional.empty());
        BDDMockito.given(workServiceMock.findAllByIds(studentCreateDTO.getWorkIds())).willReturn(new TreeSet<>(studentToReturn.getWorks()));

        StudentDTO created = studentService.create(studentCreateDTO);
        assertEquals(studentDTOToReceive, created);

        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(studentToReturn, argumentCaptor.getValue());
    }

    @Test
    public void update() throws Exception {
        final int studentId = 2;
        Student studentOld = generateStudent(studentId);

        Student studentNewToReturn = generateStudent(studentId + 1);
        ReflectionTestUtils.setField(studentNewToReturn, "id", studentId);

        StudentCreateDTO studentNewCreateDTO = generateStudentCreateDTO(studentId + 1);

        StudentDTO studentNewDTOToReceive = generateStudentDTO(studentId + 1);
        ReflectionTestUtils.setField(studentNewDTOToReceive, "id", studentId);

        BDDMockito.given(studentRepositoryMock.save(any(Student.class))).willReturn(studentNewToReturn);
        BDDMockito.given(studentRepositoryMock.findById(studentId)).willReturn(Optional.of(studentOld));
        BDDMockito.given(workServiceMock.findAllByIds(studentNewCreateDTO.getWorkIds())).willReturn(new TreeSet<>(studentNewToReturn.getWorks()));

        StudentDTO updated = studentService.create(studentNewCreateDTO);
        assertEquals(studentNewDTOToReceive, updated);

        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(studentNewToReturn, argumentCaptor.getValue());
    }

    @Test
    public void findByUsernameAsDTO() throws Exception {
        final int studentId = 10;
        Student studentToReturn = generateStudent(studentId);
        final String studentUsername = studentToReturn.getUsername();
        StudentDTO studentDTOToReceive = generateStudentDTO(studentId);

        BDDMockito.given(studentRepositoryMock.findByUsername(studentUsername)).willReturn(Optional.of(studentToReturn));

        StudentDTO receivedDTO = studentService.findByUsernameAsDTO(studentUsername).orElseThrow();
        assertEquals(studentDTOToReceive, receivedDTO);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findByUsername(argumentCaptor.capture());
        assertEquals(studentUsername, argumentCaptor.getValue());
    }

    @Test
    public void findAllByNameAsDTO() throws Exception {
        final int allStudentsCnt = 5;
        String wantedName = generateStudent(allStudentsCnt).getName();
        Set<Student> allStudentsToReturn = fillStudentCollection(new TreeSet<>(), allStudentsCnt);
        setFieldForAll(allStudentsToReturn, "name", wantedName);
        Set<StudentDTO> allStudentsToReceive = fillStudentDTOCollection(new TreeSet<>(), allStudentsCnt);
        setFieldForAll(allStudentsToReceive, "name", wantedName);

        BDDMockito.given(studentRepositoryMock.findAllByName(wantedName)).willReturn(allStudentsToReturn);

        Set<StudentDTO> receivedStudents = studentService.findAllByNameAsDTO(wantedName);
        assertEquals(allStudentsToReceive, receivedStudents);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findAllByName(idCaptor.capture());
        assertEquals(wantedName, idCaptor.getValue());
    }

    @Test
    public void joinWork() throws Exception {
        final int studentId = 1;
        final int workId = 1;
        Student student = generateStudent(studentId);
        Work work = generateWork(workId);
        Work updatedWork = generateWork(workId);
        updatedWork.getAuthors().add(student);

        BDDMockito.given(workServiceMock.findById(workId)).willReturn(Optional.of(work));
        BDDMockito.given(workServiceMock.update(any(Integer.class), any(WorkCreateDTO.class))).willReturn(updatedWork.toDTO());
        BDDMockito.given(studentRepositoryMock.findById(studentId)).willReturn(Optional.of(student));

        studentService.joinWork(studentId, workId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workServiceMock, Mockito.atLeastOnce()).findById(idCaptor.capture());
        assertEquals(workId, idCaptor.getValue());

        ArgumentCaptor<WorkCreateDTO> workCreateDTOArgumentCaptor = ArgumentCaptor.forClass(WorkCreateDTO.class);
        Mockito.verify(workServiceMock, Mockito.times(1)).update(idCaptor.capture(), workCreateDTOArgumentCaptor.capture());
        assertEquals(workId, idCaptor.getValue());
        assertEquals(updatedWork.toCreateDTO(), workCreateDTOArgumentCaptor.getValue());

        Mockito.verify(studentRepositoryMock, Mockito.atLeastOnce()).findById(idCaptor.capture());
        assertEquals(studentId, idCaptor.getValue());
    }
}