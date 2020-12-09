package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        BDDMockito.given(studentServiceMock.findByIds(authorsIds)).willReturn(authors);

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
        BDDMockito.given(studentServiceMock.findByIds(newAuthorsIds)).willReturn(newAuthors);
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
}