package cz.cvut.fit.baklaal1.business.service;

import cz.cvut.fit.baklaal1.business.repository.WorkRepository;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
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

    @BeforeEach
    void setUp() {
        Student ID10 = new Student("perfect", "Perfect", null, 1);
        ReflectionTestUtils.setField(ID10, "id", 10);
        Set<Student> authors = new TreeSet<>();
        authors.add(ID10);

        BDDMockito.given(studentServiceMock.findByIds(any())).willReturn(authors);
    }

    @Test
    void create() throws Exception {
        Work workToReturn = new Work();

        ReflectionTestUtils.setField(workToReturn, "id", 1337);
        Set<Integer> authorsIds = new HashSet<>();
        authorsIds.add(10);
        WorkCreateDTO workCreateDTO = new WorkCreateDTO("Title", "Text", authorsIds, null);

        BDDMockito.given(workRepositoryMock.save(any(Work.class))).willReturn(workToReturn);

        WorkDTO returnedWorkDTO = workService.create(workCreateDTO);

        WorkDTO expectedWorkDTO = new WorkDTO(1337, "Title", "Text", authorsIds, null);
        assertEquals(expectedWorkDTO, returnedWorkDTO);

        ArgumentCaptor<Work> argumentCaptor = ArgumentCaptor.forClass(Work.class);
        Mockito.verify(workRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());
        Work workProvidedToSave = argumentCaptor.getValue();
        assertEquals("Title", workProvidedToSave.getTitle());
        assertEquals("Text", workProvidedToSave.getText());
    }

    @Test
    void update() {
    }
}