package cz.cvut.fit.baklaal1.server.business.service;

import cz.cvut.fit.baklaal1.server.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
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
@DisplayName("WorkService CreateUpdate Test")
class WorkServiceTest {
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
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int authorId = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work work1 = new Work(title1, text1, authors, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, authors, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        List<Work> allWorks = new ArrayList<>();
        allWorks.add(work1);
        allWorks.add(work2);

        BDDMockito.given(workRepositoryMock.findAll()).willReturn(new ArrayList<>(allWorks));

        List<Work> returnedWorks = workService.findAll();
        assertEquals(allWorks, returnedWorks);

        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllAsDTO() {
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int authorId = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work work1 = new Work(title1, text1, authors, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, authors, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        List<Work> allWorks = new ArrayList<>();
        allWorks.add(work1);
        allWorks.add(work2);

        BDDMockito.given(workRepositoryMock.findAll()).willReturn(new ArrayList<>(allWorks));

        Set<Integer> authorIds = new TreeSet<>();
        authorIds.add(authorId);
        WorkDTO work1DTO = new WorkDTO(work1Id, title1, text1, authorIds, null);
        WorkDTO work2DTO = new WorkDTO(work2Id, title2, text2, authorIds, null);
        List<WorkDTO> expectedWorksAsDTO = new ArrayList<>();
        expectedWorksAsDTO.add(work1DTO);
        expectedWorksAsDTO.add(work2DTO);

        List<WorkDTO> returnedWorksAsDTO = workService.findAllAsDTO();
        assertEquals(expectedWorksAsDTO, returnedWorksAsDTO);

        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void pageAll() {
        final int page = 0;
        final int size = 2;
        final int total = 8;

        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int authorId = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work work1 = new Work(title1, text1, authors, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, authors, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        final Pageable pageableRequested =  PageRequest.of(page, size);
        final List<Work> data = List.of(work1, work2);
        final Page<Work> pageExpected = new PageImpl<>(data, pageableRequested, total);
        final Page<Work> pageToReturn = new PageImpl<>(data, pageableRequested, total);

        BDDMockito.given(workRepositoryMock.findAll(any(Pageable.class))).willReturn(pageToReturn);

        final Page<Work> pageReturned = workService.pageAll(pageableRequested);
        assertEquals(pageExpected, pageReturned);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAll(pageableArgumentCaptor.capture());
        Pageable pageableProvided = pageableArgumentCaptor.getValue();
        assertEquals(pageableRequested, pageableProvided);
    }

    @Test
    public void findAllByIds() {
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int authorId = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work work1 = new Work(title1, text1, authors, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, authors, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Set<Work> allWorksByIdExpected = new TreeSet<>();
        allWorksByIdExpected.add(work1);
        allWorksByIdExpected.add(work2);

        Set<Integer> wantedIds = new TreeSet<>();
        wantedIds.add(work1Id);
        wantedIds.add(work2Id);

        BDDMockito.given(workRepositoryMock.findAllById(wantedIds)).willReturn(new ArrayList<>(allWorksByIdExpected));
        Set<Work> allWorksByIdsGivenProvided = workService.findAllByIds(new TreeSet<>(wantedIds));
        assertEquals(allWorksByIdExpected, allWorksByIdsGivenProvided);

        ArgumentCaptor<Iterable<Integer>> idsCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAllById(idsCaptor.capture());
        Iterable<Integer> wantedIdsProvided = idsCaptor.getValue();
        assertEquals(wantedIds, wantedIdsProvided);
    }

    @Test
    public void findAllByIdsAsDTO() {
        final String title1 = "Title1";
        final String text1 = "Text1";
        final String title2 = "Title2";
        final String text2 = "Text2";
        final int authorId = 10;
        final int work1Id = 1337;
        final int work2Id = 322;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);
        Work work1 = new Work(title1, text1, authors, null);
        ReflectionTestUtils.setField(work1, "id", work1Id);
        Work work2 = new Work(title2, text2, authors, null);
        ReflectionTestUtils.setField(work2, "id", work2Id);

        Set<Work> allWorksByIdExpected = new TreeSet<>();
        allWorksByIdExpected.add(work1);
        allWorksByIdExpected.add(work2);

        Set<Integer> wantedIds = new TreeSet<>();
        wantedIds.add(work1Id);
        wantedIds.add(work2Id);

        Set<WorkDTO> allWorksByIdExpectedAsDTO = new TreeSet<>();
        allWorksByIdExpectedAsDTO.add(work1.toDTO());
        allWorksByIdExpectedAsDTO.add(work2.toDTO());

        BDDMockito.given(workRepositoryMock.findAllById(wantedIds)).willReturn(new ArrayList<>(allWorksByIdExpected));
        Set<WorkDTO> allWorksByIdsGivenProvided = workService.findAllByIdsAsDTO(new TreeSet<>(wantedIds));
        assertEquals(allWorksByIdExpectedAsDTO, allWorksByIdsGivenProvided);

        ArgumentCaptor<Iterable<Integer>> idsCaptor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findAllById(idsCaptor.capture());
        Iterable<Integer> wantedIdsProvided = idsCaptor.getValue();
        assertEquals(wantedIds, wantedIdsProvided);
    }

    @Test
    public void findById() {
        final String title = "Title1";
        final String text = "Text1";
        final int authorId = 10;
        final int workId = 1337;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);

        Work workExpected = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workExpected, "id", workId);
        Work workToReturn = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workToReturn, "id", workId);

        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workToReturn));
        Work workProvided = workService.findById(workId).orElseThrow();
        assertEquals(workExpected, workProvided);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(workId, idCaptured);
    }

    @Test
    public void findByIdAsDTO() {
        final String title = "Title1";
        final String text = "Text1";
        final int authorId = 10;
        final int workId = 1337;

        Student author = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(author, "id", authorId);
        Set<Student> authors = new TreeSet<>();
        authors.add(author);

        Set<Integer> authorIds = new TreeSet<>();
        authorIds.add(authorId);

        WorkDTO workDTOExpected = new WorkDTO(workId, title, text, authorIds, null);
        Work workToReturn = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workToReturn, "id", workId);

        BDDMockito.given(workRepositoryMock.findById(workId)).willReturn(Optional.of(workToReturn));
        WorkDTO workDTOProvided = workService.findByIdAsDTO(workId).orElseThrow();
        assertEquals(workDTOExpected, workDTOProvided);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).findById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(workId, idCaptured);
    }

    @Test
    public void delete() {
        final int workId = 1337;

        BDDMockito.doNothing().when(workRepositoryMock).deleteById(workId);

        workService.delete(workId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(workRepositoryMock, Mockito.times(1)).deleteById(idCaptor.capture());
        Integer idCaptured = idCaptor.getValue();
        assertEquals(workId, idCaptured);
    }

    @Test
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
        //TODO find out if mock is the same as in the create() method test, could be counting previous calls as well
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Work workProvidedToSave = argumentCaptor.getValue();
        assertEquals(newTitle, workProvidedToSave.getTitle());
        assertEquals(newText, workProvidedToSave.getText());
    }

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