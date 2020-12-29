package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.entity.Teacher;
import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import cz.cvut.fit.baklaal1.server.business.repository.TeacherRepository;
import cz.cvut.fit.baklaal1.server.suite.TeacherTestSuite;
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
@DisplayName("TeacherService Test")
class TeacherServiceTest extends TeacherTestSuite {
    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepositoryMock;

    @MockBean
    private AssessmentService assessmentServiceMock;

    @Test
    public void findAll() {
        final int allTeachersCnt = 10;
        List<Teacher> allTeachersToReturn = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);
        List<Teacher> allTeachersToReceive = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);

        BDDMockito.given(teacherRepositoryMock.findAll()).willReturn(allTeachersToReturn);

        List<Teacher> receivedAllTeachers = teacherService.findAll();

        assertEquals(allTeachersToReceive, receivedAllTeachers);

        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final int allTeachersCnt = 10;
        List<Teacher> allTeachersToReturn = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);
        List<TeacherDTO> allTeachersToReceiveAsDTO = fillTeacherDTOCollection(new ArrayList<>(), allTeachersCnt);

        BDDMockito.given(teacherRepositoryMock.findAll()).willReturn(allTeachersToReturn);

        List<TeacherDTO> receivedAllTeachersAsDTO = teacherService.findAllAsDTO();

        assertEquals(allTeachersToReceiveAsDTO, receivedAllTeachersAsDTO);

        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void pageAll() {
        final int allTeachersCnt = 10;
        final int page = 1;
        final int pageSize = 3;
        List<Teacher> allTeachersToReturn = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);
        List<Teacher> allTeachersToReceive = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);

        final Pageable pageableRequested = PageRequest.of(page, pageSize);
        final Page<Teacher> pageExpected = new PageImpl<>(allTeachersToReceive, pageableRequested, allTeachersCnt);
        final Page<Teacher> pageToReturn = new PageImpl<>(allTeachersToReturn, pageableRequested, allTeachersCnt);

        BDDMockito.given(teacherRepositoryMock.findAll(pageableRequested)).willReturn(pageToReturn);

        final Page<Teacher> pageReceived = teacherService.pageAll(pageableRequested);

        assertEquals(pageExpected, pageReceived);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findAll(pageableArgumentCaptor.capture());
        assertEquals(pageableRequested, pageableArgumentCaptor.getValue());
    }

    @Test
    public void findAllByIds() {
        final int allTeachersCnt = 5;
        List<Teacher> allTeachersToReturn = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);
        Set<Teacher> allTeachersToReceive = fillTeacherCollection(new TreeSet<>(), allTeachersCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allTeachersCnt);

        BDDMockito.given(teacherRepositoryMock.findAllById(allWantedIds)).willReturn(allTeachersToReturn);

        Set<Teacher> receivedTeachers = teacherService.findAllByIds(new TreeSet<>(allWantedIds));
        assertEquals(allTeachersToReceive, receivedTeachers);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findAllByIdsAsDTO() {
        final int allTeachersCnt = 5;
        List<Teacher> allTeachersToReturn = fillTeacherCollection(new ArrayList<>(), allTeachersCnt);
        Set<TeacherDTO> allTeachersToReceive = fillTeacherDTOCollection(new TreeSet<>(), allTeachersCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allTeachersCnt);

        BDDMockito.given(teacherRepositoryMock.findAllById(allWantedIds)).willReturn(allTeachersToReturn);

        Set<TeacherDTO> receivedTeachers = teacherService.findAllByIdsAsDTO(new TreeSet<>(allWantedIds));
        assertEquals(allTeachersToReceive, receivedTeachers);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findById() {
        final int teacherId = 10;
        Teacher teacherToReturn = generateTeacher(teacherId);
        Teacher teacherToReceive = generateTeacher(teacherId);

        BDDMockito.given(teacherRepositoryMock.findById(teacherId)).willReturn(Optional.of(teacherToReturn));

        Teacher received = teacherService.findById(teacherId).orElseThrow();
        assertEquals(teacherToReceive, received);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(teacherId, argumentCaptor.getValue());
    }

    @Test
    public void findByIdAsDTO() {
        final int teacherId = 10;
        Teacher teacherToReturn = generateTeacher(teacherId);
        TeacherDTO teacherDTOToReceive = generateTeacherDTO(teacherId);

        BDDMockito.given(teacherRepositoryMock.findById(teacherId)).willReturn(Optional.of(teacherToReturn));

        TeacherDTO receivedDTO = teacherService.findByIdAsDTO(teacherId).orElseThrow();
        assertEquals(teacherDTOToReceive, receivedDTO);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(teacherId, argumentCaptor.getValue());
    }

    @Test
    public void delete() {
        final int teacherId = 10;

        BDDMockito.doNothing().when(teacherRepositoryMock).deleteById(teacherId);

        teacherService.delete(teacherId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(teacherRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        assertEquals(teacherId, idCaptor.getValue());
    }

    @Test
    public void create() throws Exception {
        final int teacherId = 2;
        Teacher teacherToReturn = generateTeacher(teacherId);
        TeacherCreateDTO teacherCreateDTO = generateTeacherCreateDTO(teacherId);
        TeacherDTO teacherDTOToReceive = generateTeacherDTO(teacherId);

        BDDMockito.given(teacherRepositoryMock.save(any(Teacher.class))).willReturn(teacherToReturn);
        BDDMockito.given(teacherRepositoryMock.findByUsername(teacherCreateDTO.getUsername())).willReturn(Optional.empty());
        BDDMockito.given(assessmentServiceMock.findAllByIds(teacherCreateDTO.getAssessmentIds())).willReturn(new TreeSet<>(teacherToReturn.getAssessments()));

        TeacherDTO created = teacherService.create(teacherCreateDTO);
        assertEquals(teacherDTOToReceive, created);

        ArgumentCaptor<Teacher> argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(teacherToReturn, argumentCaptor.getValue());
    }

    @Test
    public void update() throws Exception {
        final int teacherId = 2;
        Teacher teacherOld = generateTeacher(teacherId);

        Teacher teacherNewToReturn = generateTeacher(teacherId + 1);
        ReflectionTestUtils.setField(teacherNewToReturn, "id", teacherId);

        TeacherCreateDTO teacherNewCreateDTO = generateTeacherCreateDTO(teacherId + 1);

        TeacherDTO teacherNewDTOToReceive = generateTeacherDTO(teacherId + 1);
        ReflectionTestUtils.setField(teacherNewDTOToReceive, "id", teacherId);

        BDDMockito.given(teacherRepositoryMock.save(any(Teacher.class))).willReturn(teacherNewToReturn);
        BDDMockito.given(teacherRepositoryMock.findById(teacherId)).willReturn(Optional.of(teacherOld));
        BDDMockito.given(assessmentServiceMock.findAllByIds(teacherNewCreateDTO.getAssessmentIds())).willReturn(new TreeSet<>(teacherNewToReturn.getAssessments()));

        TeacherDTO updated = teacherService.create(teacherNewCreateDTO);
        assertEquals(teacherNewDTOToReceive, updated);

        ArgumentCaptor<Teacher> argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        Mockito.verify(teacherRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(teacherNewToReturn, argumentCaptor.getValue());
    }

    @Test
    public void deleteByUsername() {
        final int id = 105;
        final Teacher teacher = generateTeacher(id);
        final String username = teacher.getUsername();

        BDDMockito.given(teacherRepositoryMock.findByUsername(username)).willReturn(Optional.of(teacher));
        BDDMockito.doNothing().when(teacherRepositoryMock).deleteById(id);

        teacherService.deleteByUsername(username);

        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(teacherRepositoryMock, Mockito.times(1)).findByUsername(usernameCaptor.capture());
        assertEquals(username, usernameCaptor.getValue());

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(teacherRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }
}