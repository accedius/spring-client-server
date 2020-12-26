package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.entity.Teacher;
import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.server.business.repository.AssessmentRepository;
import cz.cvut.fit.baklaal1.server.suite.AssessmentTestSuite;
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
@DisplayName("AssessmentService Test")
class AssessmentServiceTest extends AssessmentTestSuite {
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
        final int allAssessmentsCnt = 10;
        List<Assessment> allAssessmentsToReturn = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);
        List<Assessment> allAssessmentsToReceive = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);

        BDDMockito.given(assessmentRepositoryMock.findAll()).willReturn(allAssessmentsToReturn);

        List<Assessment> receivedAllAssessments = assessmentService.findAll();

        assertEquals(allAssessmentsToReceive, receivedAllAssessments);

        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final int allAssessmentsCnt = 10;
        List<Assessment> allAssessmentsToReturn = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);
        List<AssessmentDTO> allAssessmentsToReceiveAsDTO = fillAssessmentDTOCollection(new ArrayList<>(), allAssessmentsCnt);

        BDDMockito.given(assessmentRepositoryMock.findAll()).willReturn(allAssessmentsToReturn);

        List<AssessmentDTO> receivedAllAssessmentsAsDTO = assessmentService.findAllAsDTO();

        assertEquals(allAssessmentsToReceiveAsDTO, receivedAllAssessmentsAsDTO);

        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void pageAll() {
        final int allAssessmentsCnt = 10;
        final int page = 1;
        final int pageSize = 3;
        List<Assessment> allAssessmentsToReturn = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);
        List<Assessment> allAssessmentsToReceive = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);

        final Pageable pageableRequested = PageRequest.of(page, pageSize);
        final Page<Assessment> pageExpected = new PageImpl<>(allAssessmentsToReceive, pageableRequested, allAssessmentsCnt);
        final Page<Assessment> pageToReturn = new PageImpl<>(allAssessmentsToReturn, pageableRequested, allAssessmentsCnt);

        BDDMockito.given(assessmentRepositoryMock.findAll(pageableRequested)).willReturn(pageToReturn);

        final Page<Assessment> pageReceived = assessmentService.pageAll(pageableRequested);

        assertEquals(pageExpected, pageReceived);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findAll(pageableArgumentCaptor.capture());
        assertEquals(pageableRequested, pageableArgumentCaptor.getValue());
    }

    @Test
    public void findAllByIds() {
        final int allAssessmentsCnt = 5;
        List<Assessment> allAssessmentsToReturn = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);
        Set<Assessment> allAssessmentsToReceive = fillAssessmentCollection(new TreeSet<>(), allAssessmentsCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allAssessmentsCnt);

        BDDMockito.given(assessmentRepositoryMock.findAllById(allWantedIds)).willReturn(allAssessmentsToReturn);

        Set<Assessment> receivedAssessments = assessmentService.findAllByIds(new TreeSet<>(allWantedIds));
        assertEquals(allAssessmentsToReceive, receivedAssessments);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findAllByIdsAsDTO() {
        final int allAssessmentsCnt = 5;
        List<Assessment> allAssessmentsToReturn = fillAssessmentCollection(new ArrayList<>(), allAssessmentsCnt);
        Set<AssessmentDTO> allAssessmentsToReceive = fillAssessmentDTOCollection(new TreeSet<>(), allAssessmentsCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allAssessmentsCnt);

        BDDMockito.given(assessmentRepositoryMock.findAllById(allWantedIds)).willReturn(allAssessmentsToReturn);

        Set<AssessmentDTO> receivedAssessments = assessmentService.findAllByIdsAsDTO(new TreeSet<>(allWantedIds));
        assertEquals(allAssessmentsToReceive, receivedAssessments);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findById() {
        final int assessmentId = 10;
        Assessment assessmentToReturn = generateAssessment(assessmentId);
        Assessment assessmentToReceive = generateAssessment(assessmentId);

        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn));

        Assessment received = assessmentService.findById(assessmentId).orElseThrow();
        assertEquals(assessmentToReceive, received);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(assessmentId, argumentCaptor.getValue());
    }

    @Test
    public void findByIdAsDTO() {
        final int assessmentId = 10;
        Assessment assessmentToReturn = generateAssessment(assessmentId);
        AssessmentDTO assessmentDTOToReceive = generateAssessmentDTO(assessmentId);

        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn));

        AssessmentDTO receivedDTO = assessmentService.findByIdAsDTO(assessmentId).orElseThrow();
        assertEquals(assessmentDTOToReceive, receivedDTO);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(assessmentId, argumentCaptor.getValue());
    }

    @Test
    public void delete() {
        final int assessmentId = 10;

        BDDMockito.doNothing().when(assessmentRepositoryMock).deleteById(assessmentId);

        assessmentService.delete(assessmentId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        assertEquals(assessmentId, idCaptor.getValue());
    }

    @Test
    public void create() throws Exception {
        final int assessmentId = 2;

        Assessment assessmentToReturn = generateAssessment(assessmentId);
        AssessmentCreateDTO assessmentCreateDTO = generateAssessmentCreateDTO(assessmentId);
        AssessmentDTO assessmentDTOToReceive = generateAssessmentDTO(assessmentId);

        BDDMockito.given(assessmentRepositoryMock.save(any(Assessment.class))).willReturn(assessmentToReturn);
        BDDMockito.given(assessmentRepositoryMock.findByWork(any(Work.class))).willReturn(Optional.empty());
        BDDMockito.given(teacherServiceMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn.getEvaluator()));
        BDDMockito.given(workServiceMock.findById(assessmentId)).willReturn(Optional.of(assessmentToReturn.getWork()));

        AssessmentDTO created = assessmentService.create(assessmentCreateDTO);
        assertEquals(assessmentDTOToReceive, created);

        ArgumentCaptor<Assessment> argumentCaptor = ArgumentCaptor.forClass(Assessment.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(assessmentToReturn, argumentCaptor.getValue());
    }

    @Test
    public void update() throws Exception {
        final int assessmentId = 2;
        final int newGenerator = assessmentId + 1;
        Assessment assessmentOld = generateAssessment(assessmentId);

        Assessment assessmentNewToReturn = generateAssessment(newGenerator);
        ReflectionTestUtils.setField(assessmentNewToReturn, "id", assessmentId);

        AssessmentCreateDTO assessmentNewCreateDTO = generateAssessmentCreateDTO(newGenerator);

        AssessmentDTO assessmentNewDTOToReceive = generateAssessmentDTO(newGenerator);
        ReflectionTestUtils.setField(assessmentNewDTOToReceive, "id", assessmentId);

        BDDMockito.given(assessmentRepositoryMock.save(any(Assessment.class))).willReturn(assessmentNewToReturn);
        BDDMockito.given(assessmentRepositoryMock.findById(assessmentId)).willReturn(Optional.of(assessmentOld));
        BDDMockito.given(teacherServiceMock.findById(newGenerator)).willReturn(Optional.of(assessmentNewToReturn.getEvaluator()));
        BDDMockito.given(workServiceMock.findById(newGenerator)).willReturn(Optional.of(assessmentNewToReturn.getWork()));

        AssessmentDTO updated = assessmentService.create(assessmentNewCreateDTO);
        assertEquals(assessmentNewDTOToReceive, updated);

        ArgumentCaptor<Assessment> argumentCaptor = ArgumentCaptor.forClass(Assessment.class);
        Mockito.verify(assessmentRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(assessmentNewToReturn, argumentCaptor.getValue());
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