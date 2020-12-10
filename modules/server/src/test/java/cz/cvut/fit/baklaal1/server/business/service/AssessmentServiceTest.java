package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Teacher;
import cz.cvut.fit.baklaal1.model.data.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.server.business.repository.AssessmentRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
//TODO maybe should just use one DataSource for all the Test classes, since Spring creates HikariDataSource pool for each Test class in runtime, causing opening and closing same database Connection for each Test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("AssessmentService CreateUpdate Test")
class AssessmentServiceTest {
    @Autowired
    private AssessmentService assessmentService;

    @MockBean
    private AssessmentRepository assessmentRepositoryMock;

    @MockBean
    private TeacherService teacherServiceMock;

    @MockBean
    private WorkService workServiceMock;

    @Test
    public void findAll() {
        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 59999.99);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        List<Assessment> allAssessments = new ArrayList<>();
        allAssessments.add(assessment1);
        allAssessments.add(assessment2);

        BDDMockito.given(assessmentRepositoryMock.findAll()).willReturn(new ArrayList<>(allAssessments));

        List<Assessment> returnedAssessments = assessmentService.findAll();
        assertEquals(allAssessments, returnedAssessments);

        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int work1Id = 23;
        final int work2Id = 75;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        List<Assessment> allAssessments = new ArrayList<>();
        allAssessments.add(assessment1);
        allAssessments.add(assessment2);

        BDDMockito.given(assessmentRepositoryMock.findAll()).willReturn(new ArrayList<>(allAssessments));

        AssessmentDTO assessment1DTO = new AssessmentDTO(assessment1Id, grade1, work1.getId(), teacher.getId());
        AssessmentDTO assessment2DTO = new AssessmentDTO(assessment2Id, grade2, work2.getId(), teacher.getId());
        List<AssessmentDTO> expectedAssessmentsAsDTO = new ArrayList<>();
        expectedAssessmentsAsDTO.add(assessment1DTO);
        expectedAssessmentsAsDTO.add(assessment2DTO);

        List<AssessmentDTO> returnedAssessmentsAsDTO = assessmentService.findAllAsDTO();
        assertEquals(expectedAssessmentsAsDTO, returnedAssessmentsAsDTO);

        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void pageAll() {
        final int page = 0;
        final int size = 2;
        final int total = 8;

        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int work1Id = 23;
        final int work2Id = 75;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        final Pageable pageableRequested =  PageRequest.of(page, size);
        final List<Assessment> data = List.of(assessment1, assessment2);
        final Page<Assessment> pageExpected = new PageImpl<>(data, pageableRequested, total);
        final Page<Assessment> pageToReturn = new PageImpl<>(data, pageableRequested, total);

        BDDMockito.given(assessmentRepositoryMock.findAll(any(Pageable.class))).willReturn(pageToReturn);

        final Page<Assessment> pageReturned = assessmentService.pageAll(pageableRequested);
        assertEquals(pageExpected, pageReturned);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAll(pageableArgumentCaptor.capture());
        Pageable pageableProvided = pageableArgumentCaptor.getValue();
        assertEquals(pageableRequested, pageableProvided);
    }

    @Test
    public void findAllByIds() {
        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int work1Id = 23;
        final int work2Id = 75;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        Set<Assessment> allAssessmentsByIdExpected = new TreeSet<>();
        allAssessmentsByIdExpected.add(assessment1);
        allAssessmentsByIdExpected.add(assessment2);

        Set<Integer> wantedIds = new TreeSet<>();
        wantedIds.add(assessment1Id);
        wantedIds.add(assessment2Id);

        BDDMockito.given(assessmentRepositoryMock.findAllById(wantedIds)).willReturn(new ArrayList<>(allAssessmentsByIdExpected));
        Set<Assessment> allAssessmentsByIdsGivenProvided = assessmentService.findAllByIds(new TreeSet<>(wantedIds));
        assertEquals(allAssessmentsByIdExpected, allAssessmentsByIdsGivenProvided);

        ArgumentCaptor<Iterable<Integer>> idsCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAllById(idsCaptor.capture());
        Iterable<Integer> wantedIdsProvided = idsCaptor.getValue();
        assertEquals(wantedIds, wantedIdsProvided);
    }

    @Test
    public void findAllByIdsAsDTO() {
        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int work1Id = 23;
        final int work2Id = 75;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        Set<Assessment> allAssessmentsByIdExpected = new TreeSet<>();
        allAssessmentsByIdExpected.add(assessment1);
        allAssessmentsByIdExpected.add(assessment2);

        Set<Integer> wantedIds = new TreeSet<>();
        wantedIds.add(assessment1Id);
        wantedIds.add(assessment2Id);

        Set<AssessmentDTO> allAssessmentsByIdExpectedAsDTO = new TreeSet<>();
        allAssessmentsByIdExpectedAsDTO.add(assessment1.toDTO());
        allAssessmentsByIdExpectedAsDTO.add(assessment2.toDTO());

        BDDMockito.given(assessmentRepositoryMock.findAllById(wantedIds)).willReturn(new ArrayList<>(allAssessmentsByIdExpected));
        Set<AssessmentDTO> allAssessmentsByIdsGivenProvided = assessmentService.findAllByIdsAsDTO(new TreeSet<>(wantedIds));
        assertEquals(allAssessmentsByIdExpectedAsDTO, allAssessmentsByIdsGivenProvided);

        ArgumentCaptor<Iterable<Integer>> idsCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAllById(idsCaptor.capture());
        Iterable<Integer> wantedIdsProvided = idsCaptor.getValue();
        assertEquals(wantedIds, wantedIdsProvided);
    }

    @Test
    public void findById() {
        final int grade1 = 3;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final int teacherId = 10;
        final int work1Id = 23;
        final int assessmentId = 1337;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);

        Assessment assessmentToReturn = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessmentToReturn, "id", assessmentId);
        Assessment assessmentExpected = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessmentExpected, "id", assessmentId);

        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn));
        Assessment assessmentProvided = assessmentService.findById(assessmentId).orElseThrow();
        assertEquals(assessmentExpected, assessmentProvided);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(assessmentId, idCaptured);
    }

    @Test
    public void findByIdAsDTO() {
        final int grade1 = 3;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final int teacherId = 10;
        final int work1Id = 23;
        final int assessmentId = 1337;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);

        Assessment assessmentToReturn = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessmentToReturn, "id", assessmentId);
        AssessmentDTO assessmentDTOExpected = new AssessmentDTO(assessmentId, grade1, work1.getId(), teacher.getId());

        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn));
        AssessmentDTO assessmentDTOProvided = assessmentService.findByIdAsDTO(assessmentId).orElseThrow();
        assertEquals(assessmentDTOExpected, assessmentDTOProvided);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(assessmentId, idCaptured);
    }

    @Test
    public void delete() {
        final int assessmentId = 1337;

        BDDMockito.doNothing().when(assessmentRepositoryMock).deleteById(assessmentId);

        assessmentService.delete(assessmentId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(assessmentId, idCaptured);
    }

    @Test
    public void create() throws Exception {
        final int grade = 3;
        final String title = "Title1";
        final String text = "Text1";
        final int teacherId = 10;
        final int workId = 23;
        final int assessmentId = 1337;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work = new Work(title, text, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work, "id", workId);

        Assessment assessmentToReturn = new Assessment(grade, work, teacher);
        ReflectionTestUtils.setField(assessmentToReturn, "id", assessmentId);

        AssessmentCreateDTO assessmentCreateDTO = new AssessmentCreateDTO(grade, workId, teacherId);

        BDDMockito.given(assessmentRepositoryMock.save(any(Assessment.class))).willReturn(assessmentToReturn);
        BDDMockito.given(teacherServiceMock.findById(teacherId)).willReturn(Optional.of(teacher));
        BDDMockito.given(workServiceMock.findById(workId)).willReturn(Optional.of(work));

        AssessmentDTO returnedAssessmentDTO = assessmentService.create(assessmentCreateDTO);

        AssessmentDTO expectedAssessmentDTO = new AssessmentDTO(assessmentId, grade, workId, teacherId);
        assertEquals(expectedAssessmentDTO, returnedAssessmentDTO);

        ArgumentCaptor<Assessment> argumentCaptor = ArgumentCaptor.forClass(Assessment.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Assessment assessmentProvidedToSave = argumentCaptor.getValue();
        assertEquals(grade, assessmentProvidedToSave.getGrade());
        assertEquals(teacherId, assessmentProvidedToSave.getEvaluator().getId());
        assertEquals(workId, assessmentProvidedToSave.getWork().getId());
    }

    @Test
    public void update() throws Exception {
        final int grade = 3;
        final String title = "Title1";
        final String text = "Text1";
        final int teacherId = 10;
        final int workId = 23;
        final int assessmentId = 1337;
        final int newGrade = 4;
        final int newTeacherId = 20;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Teacher newTeacher = new Teacher("good", "Good", null, 1);
        ReflectionTestUtils.setField(newTeacher, "id", newTeacherId);

        Work work = new Work(title, text, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work, "id", workId);

        Assessment assessmentToBeUpdated = new Assessment(grade, work, teacher);
        ReflectionTestUtils.setField(assessmentToBeUpdated, "id", assessmentId);

        Assessment assessmentToReturn = new Assessment(newGrade, work, newTeacher);
        ReflectionTestUtils.setField(assessmentToReturn, "id", assessmentId);

        AssessmentCreateDTO assessmentCreateDTO = new AssessmentCreateDTO(grade, workId, teacherId);

        AssessmentCreateDTO assessmentToUpdateDTO = new AssessmentCreateDTO(newGrade, workId, newTeacherId);

        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentToBeUpdated));
        BDDMockito.given(assessmentRepositoryMock.save(any(Assessment.class))).willReturn(assessmentToReturn);
        BDDMockito.given(teacherServiceMock.findById(newTeacherId)).willReturn(Optional.of(newTeacher));
        BDDMockito.given(workServiceMock.findById(workId)).willReturn(Optional.of(work));

        AssessmentDTO returnedAssessmentDTO = assessmentService.update(assessmentId, assessmentToUpdateDTO);

        AssessmentDTO expectedAssessmentDTO = new AssessmentDTO(assessmentId, newGrade, workId, newTeacherId);
        assertEquals(expectedAssessmentDTO, returnedAssessmentDTO);

        ArgumentCaptor<Assessment> argumentCaptor = ArgumentCaptor.forClass(Assessment.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Assessment assessmentProvidedToSave = argumentCaptor.getValue();
        assertEquals(newGrade, assessmentProvidedToSave.getGrade());
        assertEquals(newTeacherId, assessmentProvidedToSave.getEvaluator().getId());
    }

    @Test
    public void findAllByEvaluatorIdAsDTO() {
        final int grade1 = 3;
        final int grade2 = 5;
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int teacherId = 10;
        final int work1Id = 23;
        final int work2Id = 75;
        final int assessment1Id = 1337;
        final int assessment2Id = 322;

        Teacher teacher = new Teacher("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(teacher, "id", teacherId);

        Work work1 = new Work(title1, text1, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, new TreeSet<>(), null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Assessment assessment1 = new Assessment(grade1, work1, teacher);
        ReflectionTestUtils.setField(assessment1, "id", assessment1Id);
        Assessment assessment2 = new Assessment(grade2, work2, teacher);
        ReflectionTestUtils.setField(assessment2, "id", assessment2Id);

        Set<Assessment> allAssessmentsByTitleExpected = new TreeSet<>();
        allAssessmentsByTitleExpected.add(assessment1);
        allAssessmentsByTitleExpected.add(assessment2);

        Set<AssessmentDTO> allAssessmentsByIdExpectedAsDTO = new TreeSet<>();
        allAssessmentsByIdExpectedAsDTO.add(assessment1.toDTO());
        allAssessmentsByIdExpectedAsDTO.add(assessment2.toDTO());

        BDDMockito.given(assessmentRepositoryMock.findAllByEvaluatorId(teacherId)).willReturn(new TreeSet<>(allAssessmentsByTitleExpected));
        Set<AssessmentDTO> allAssessmentsByEvaluatorIdGivenProvided = assessmentService.findAllByEvaluatorIdAsDTO(teacherId);
        assertEquals(allAssessmentsByIdExpectedAsDTO, allAssessmentsByEvaluatorIdGivenProvided);

        ArgumentCaptor<Integer> titleCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).findAllByEvaluatorId(titleCaptor.capture());
        Integer idProvided = titleCaptor.getValue();
        assertEquals(teacherId, idProvided);
    }
}