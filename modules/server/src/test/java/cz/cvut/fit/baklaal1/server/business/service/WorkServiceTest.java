package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.server.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.server.suite.WorkTestSuite;
import org.hibernate.mapping.Any;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
//TODO maybe should just use one DataSource for all the Test classes, since Spring creates HikariDataSource pool for each Test class in runtime, causing opening and closing same database Connection for each Test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("WorkService Test")
class WorkServiceTest extends WorkTestSuite {
    @Autowired
    private WorkService workService;

    @MockBean
    private WorkRepository workRepositoryMock;

    @MockBean
    private StudentService studentServiceMock;

    @MockBean
    private AssessmentService assessmentServiceMock;

    @Test
    public void findAll() {
        final int allWorksCnt = 10;
        List<Work> allWorksToReturn = fillWorkCollection(new ArrayList<>(), allWorksCnt);
        List<Work> allWorksToReceive = fillWorkCollection(new ArrayList<>(), allWorksCnt);

        BDDMockito.given(workRepositoryMock.findAll()).willReturn(allWorksToReturn);

        List<Work> receivedAllWorks = workService.findAll();

        assertEquals(allWorksToReceive, receivedAllWorks);

        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final int allWorksCnt = 10;
        List<Work> allWorksToReturn = fillWorkCollection(new ArrayList<>(), allWorksCnt);
        List<WorkDTO> allWorksToReceiveAsDTO = fillWorkDTOCollection(new ArrayList<>(), allWorksCnt);

        BDDMockito.given(workRepositoryMock.findAll()).willReturn(allWorksToReturn);

        List<WorkDTO> receivedAllWorksAsDTO = workService.findAllAsDTO();

        assertEquals(allWorksToReceiveAsDTO, receivedAllWorksAsDTO);

        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void pageAll() {
        final int allWorksCnt = 10;
        final int page = 1;
        final int pageSize = 3;
        List<Work> allWorksToReturn = fillWorkCollection(new ArrayList<>(), allWorksCnt);
        List<Work> allWorksToReceive = fillWorkCollection(new ArrayList<>(), allWorksCnt);

        final Pageable pageableRequested = PageRequest.of(page, pageSize);
        final Page<Work> pageExpected = new PageImpl<>(allWorksToReceive, pageableRequested, allWorksCnt);
        final Page<Work> pageToReturn = new PageImpl<>(allWorksToReturn, pageableRequested, allWorksCnt);

        BDDMockito.given(workRepositoryMock.findAll(pageableRequested)).willReturn(pageToReturn);

        final Page<Work> pageReceived = workService.pageAll(pageableRequested);

        assertEquals(pageExpected, pageReceived);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findAll(pageableArgumentCaptor.capture());
        assertEquals(pageableRequested, pageableArgumentCaptor.getValue());
    }

    @Test
    public void findAllByIds() {
        final int allWorksCnt = 5;
        List<Work> allWorksToReturn = fillWorkCollection(new ArrayList<>(), allWorksCnt);
        Set<Work> allWorksToReceive = fillWorkCollection(new TreeSet<>(), allWorksCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allWorksCnt);

        BDDMockito.given(workRepositoryMock.findAllById(allWantedIds)).willReturn(allWorksToReturn);

        Set<Work> receivedWorks = workService.findAllByIds(new TreeSet<>(allWantedIds));
        assertEquals(allWorksToReceive, receivedWorks);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findAllByIdsAsDTO() {
        final int allWorksCnt = 5;
        List<Work> allWorksToReturn = fillWorkCollection(new ArrayList<>(), allWorksCnt);
        Set<WorkDTO> allWorksToReceive = fillWorkDTOCollection(new TreeSet<>(), allWorksCnt);
        Set<Integer> allWantedIds = fillIntegerCollectionUpTo(new HashSet<>(), allWorksCnt);

        BDDMockito.given(workRepositoryMock.findAllById(allWantedIds)).willReturn(allWorksToReturn);

        Set<WorkDTO> receivedWorks = workService.findAllByIdsAsDTO(new TreeSet<>(allWantedIds));
        assertEquals(allWorksToReceive, receivedWorks);

        ArgumentCaptor<Iterable<Integer>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findAllById(argumentCaptor.capture());
        assertEquals(allWantedIds, argumentCaptor.getValue());
    }

    @Test
    public void findById() {
        final int workId = 10;
        Work workToReturn = generateWork(workId);
        Work workToReceive = generateWork(workId);

        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workToReturn));

        Work received = workService.findById(workId).orElseThrow();
        assertEquals(workToReceive, received);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(workId, argumentCaptor.getValue());
    }

    @Test
    public void findByIdAsDTO() {
        final int workId = 10;
        Work workToReturn = generateWork(workId);
        WorkDTO workDTOToReceive = generateWorkDTO(workId);

        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workToReturn));

        WorkDTO receivedDTO = workService.findByIdAsDTO(workId).orElseThrow();
        assertEquals(workDTOToReceive, receivedDTO);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).findById(argumentCaptor.capture());
        assertEquals(workId, argumentCaptor.getValue());
    }

    @Test
    public void delete() {
        final int workId = 10;

        BDDMockito.doNothing().when(workRepositoryMock).deleteById(workId);

        workService.delete(workId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        assertEquals(workId, idCaptor.getValue());
    }

    @Test
    public void create() throws Exception {
        final int workId = 2;
        Work workToReturn = generateWork(workId);

        Set<Student> authors = new TreeSet<>();
        Student author = generateStudent(workId);
        authors.add(author);
        workToReturn.setAuthors(authors);

        WorkCreateDTO workCreateDTO = workToReturn.toCreateDTO();
        WorkDTO workDTOToReceive = workToReturn.toDTO();

        BDDMockito.given(workRepositoryMock.save(any(Work.class))).willReturn(workToReturn);
        BDDMockito.given(workRepositoryMock.findAllByTitleAndAuthorsIn(any(String.class), any())).willReturn(new TreeSet<>());
        BDDMockito.given(studentServiceMock.findAllByIds(workCreateDTO.getAuthorIds())).willReturn(workToReturn.getAuthors());

        WorkDTO created = workService.create(workCreateDTO);
        assertEquals(workDTOToReceive, created);

        ArgumentCaptor<Work> argumentCaptor = ArgumentCaptor.forClass(Work.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(workToReturn, argumentCaptor.getValue());
    }

    @Test
    public void update() throws Exception {
        final int workId = 2;
        final int newGenerator = workId + 1;
        Work workOld = generateWork(workId);

        Assessment newAssessment = generateAssessment(newGenerator);

        Work workNewToReturn = generateWork(newGenerator);
        ReflectionTestUtils.setField(workNewToReturn, "id", workId);
        workNewToReturn.setAssessment(newAssessment);

        Set<Student> authors = new TreeSet<>();
        Student author = generateStudent(workId);
        authors.add(author);
        workNewToReturn.setAuthors(authors);

        newAssessment.setWork(workNewToReturn);

        WorkCreateDTO workNewCreateDTO = workNewToReturn.toCreateDTO();
        WorkDTO workNewDTOToReceive = workNewToReturn.toDTO();

        BDDMockito.given(workRepositoryMock.save(any(Work.class))).willReturn(workNewToReturn);
        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workOld));
        BDDMockito.given(studentServiceMock.findAllByIds(workNewCreateDTO.getAuthorIds())).willReturn(workNewToReturn.getAuthors());
        BDDMockito.given(assessmentServiceMock.findById(newGenerator)).willReturn(Optional.of(newAssessment));

        WorkDTO updated = workService.create(workNewCreateDTO);
        assertEquals(workNewDTOToReceive, updated);

        ArgumentCaptor<Work> argumentCaptor = ArgumentCaptor.forClass(Work.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        assertEquals(workNewToReturn, argumentCaptor.getValue());
    }

    /*@Test
    public void create() throws Exception {
        final String title = "Title";
        final String text = "Text";
        final int authorId = 10;
        final int workId = 1337;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work workToReturn = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workToReturn, "id", workId);

        Set<Integer> authorsIds = new HashSet<>();
        authorsIds.add(authorId);
        WorkCreateDTO workCreateDTO = new WorkCreateDTO(title, text, authorsIds, null);

        BDDMockito.given(workRepositoryMock.save(any(Work.class))).willReturn(workToReturn);
        BDDMockito.given(workRepositoryMock.findAllByTitleAndAuthorsIn(any(String.class), any())).willReturn(new TreeSet<>());
        BDDMockito.given(studentServiceMock.findAllByIds(authorsIds)).willReturn(authors);

        WorkDTO returnedWorkDTO = workService.create(workCreateDTO);

        WorkDTO expectedWorkDTO = new WorkDTO(workId, title, text, authorsIds, null);
        assertEquals(expectedWorkDTO, returnedWorkDTO);

        ArgumentCaptor<Work> argumentCaptor = ArgumentCaptor.forClass(Work.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Work workProvidedToSave = argumentCaptor.getValue();
        assertEquals(title, workProvidedToSave.getTitle());
        assertEquals(text, workProvidedToSave.getText());
    }

    @Test
    public void update() throws Exception {
        final String title = "Title";
        final String text = "Text";
        final int authorId = 10;
        final String newTitle = "New Title";
        final String newText = "New Text";
        final int newAuthorId = 20;
        final int newAssessmentId = 3;
        final int workId = 1337;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work workToBeUpdated = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workToBeUpdated, "id", workId);

        Student newAuthor = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(newAuthor, "id", newAuthorId);
        Set<Student> newAuthors = new TreeSet<>();
        newAuthors.add(newAuthor);
        Work workToReturn = new Work(newTitle, newText, newAuthors, null);
        ReflectionTestUtils.setField(workToReturn, "id", workId);

        Assessment newAssessment = new Assessment(2, workToReturn, null);
        ReflectionTestUtils.setField(newAssessment, "id", newAssessmentId);
        workToReturn.setAssessment(newAssessment);

        Set<Integer> newAuthorsIds = new HashSet<>();
        newAuthorsIds.add(newAuthorId);
        WorkCreateDTO workToUpdateDTO = new WorkCreateDTO(newTitle, newText, newAuthorsIds, newAssessmentId);

        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workToBeUpdated));
        BDDMockito.given(workRepositoryMock.save(any(Work.class))).willReturn(workToReturn);
        BDDMockito.given(studentServiceMock.findAllByIds(newAuthorsIds)).willReturn(newAuthors);
        BDDMockito.given(assessmentServiceMock.findById(newAssessmentId)).willReturn(Optional.of(newAssessment));

        WorkDTO returnedWorkDTO = workService.update(workId, workToUpdateDTO);

        WorkDTO expectedWorkDTO = new WorkDTO(workId, newTitle, newText, newAuthorsIds, newAssessmentId);
        assertEquals(expectedWorkDTO, returnedWorkDTO);

        ArgumentCaptor<Work> argumentCaptor = ArgumentCaptor.forClass(Work.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Work workProvidedToSave = argumentCaptor.getValue();
        assertEquals(newTitle, workProvidedToSave.getTitle());
        assertEquals(newText, workProvidedToSave.getText());
    }*/

    @Test
    public void findAllByTitleAsDTO() {
        String wantedTitle = "Super Title";
        final String title1 = wantedTitle;
        final String text1 = "Text1";
        final String title2 = wantedTitle;
        final String text2 = "Text2";
        final int author1Id = 10;
        final int author2Id = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author1 = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author1, "id", author1Id);
        Student author2 = new Student("super", "Super", null, 2);
        ReflectionTestUtils.setField(author2, "id", author2Id);

        Set<Student> authors1 = new TreeSet<>();
        authors1.add(author1);
        Work work1 = new Work(title1, text1, authors1, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);

        Set<Student> authors2 = new TreeSet<>();
        authors2.add(author2);
        Work work2 = new Work(title2, text2, authors2, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Set<Work> allWorksByTitleExpected = new TreeSet<>();
        allWorksByTitleExpected.add(work1);
        allWorksByTitleExpected.add(work2);

        Set<WorkDTO> allWorksByIdExpectedAsDTO = new TreeSet<>();
        allWorksByIdExpectedAsDTO.add(work1.toDTO());
        allWorksByIdExpectedAsDTO.add(work2.toDTO());

        BDDMockito.given(workRepositoryMock.findAllByTitle(wantedTitle)).willReturn(new TreeSet<>(allWorksByTitleExpected));
        Set<WorkDTO> allWorksByIdsGivenProvided = workService.findAllByTitleAsDTO(wantedTitle);
        assertEquals(allWorksByIdExpectedAsDTO, allWorksByIdsGivenProvided);

        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAllByTitle(titleCaptor.capture());
        String titleProvided = titleCaptor.getValue();
        assertEquals(wantedTitle, titleProvided);
    }
}