package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.server.business.service.StudentService;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionEntityAlreadyExists;
import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.server.suite.StudentTestSuite;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@DisplayName("StudentController Test")
class StudentControllerTest extends StudentTestSuite {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentServiceMock;

    @Test
    public void postConflict() throws Exception {
        final int studentId = 1;
        final StudentCreateDTO studentCreateDTO = generateStudentCreateDTO(studentId);

        BDDMockito.given(studentServiceMock.create(studentCreateDTO)).willThrow(new ServiceExceptionEntityAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateStudentCreateDTOJson(studentId)))

                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void postCreated() throws Exception {
        final int studentId = 1;
        final StudentCreateDTO studentCreateDTO = generateStudentCreateDTO(studentId);
        final StudentDTO studentDTO = generateStudentDTO(studentId);

        BDDMockito.given(studentServiceMock.create(studentCreateDTO)).willReturn(studentDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateStudentCreateDTOJson(studentId)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.endsWith(postAddress + "/" + studentId)));
    }

    @Test
    public void getOne() throws Exception {
        final int studentId = 1;
        final Student student = generateStudent(studentId);

        BDDMockito.given(studentServiceMock.findById(studentId)).willReturn(Optional.of(student));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", studentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, student.toDTO());
    }

    @Test
    public void getOneNotFound() throws Exception {
        final int studentId = 1;

        BDDMockito.given(studentServiceMock.findById(studentId)).willReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", studentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOneLinks() throws Exception {
        final int studentId = 1;
        final Student student = generateStudent(studentId);

        BDDMockito.given(studentServiceMock.findById(studentId)).willReturn(Optional.of(student));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", studentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + studentId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
        checkResponse(result, student.toDTO());
    }

    @Test
    public void getPage() throws Exception {
        final int page = 1;
        final int size = 3;
        final int studentsTotal = 8;
        final Pageable pageable =  PageRequest.of(page, size);
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            Student student = generateStudent(i);
            students.add(student);
        }
        final Page<Student> pageExpected = new PageImpl<>(students, pageable, studentsTotal);

        BDDMockito.given(studentServiceMock.pageAll(pageable)).willReturn(pageExpected);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "?page={page}&size={size}", page, size))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", CoreMatchers.containsString("page=2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", CoreMatchers.containsString("page=2")));

        checkDTOPage(result, students);
    }

    @Test
    public void getAll() throws Exception {
        final int studentsTotal = 8;
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentsTotal ; i++) {
            Student student = generateStudent(i);
            students.add(student);
        }

        BDDMockito.given(studentServiceMock.findAll()).willReturn(students);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/all")
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, students);
    }

    @Test
    public void update() throws Exception {
        final int studentId = 1;
        final StudentCreateDTO updatedStudentCreateDTO = generateStudentCreateDTO(studentId+1);
        final StudentDTO updatedStudentDTO = generateStudentDTO(studentId+1);
        setField(updatedStudentDTO, "id", studentId);

        BDDMockito.given(studentServiceMock.update(studentId, updatedStudentCreateDTO)).willReturn(updatedStudentDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + "/{id}", studentId)
                .contentType("application/json")
                .content(generateStudentCreateDTOJson(studentId+1)))

                .andExpect(MockMvcResultMatchers.status().isAccepted());
        checkResponse(result, updatedStudentDTO);
    }

    @Test
    public void delete() throws Exception {
        final int studentId = 1;

        BDDMockito.doNothing().when(studentServiceMock).delete(studentId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "/{id}", studentId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteByUsername() throws Exception {
        final String username = "username";

        BDDMockito.doNothing().when(studentServiceMock).deleteByUsername(username);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "?username={username}", username))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void readByUsername() throws Exception {
        final int studentId = 1;
        final Student student = generateStudent(studentId);

        BDDMockito.given(studentServiceMock.findByUsernameAsDTO(student.getUsername())).willReturn(Optional.of(student.toDTO()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + StudentController.READ_BY_USERNAME + "?username={username}", student.getUsername())
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, student.toDTO());
    }

    @Test
    public void readAllByName() throws Exception {
        final int studentsTotal = 8;
        final String wantedName = "SpecificName";
        List<Student> students = new ArrayList<>();
        Set<StudentDTO> studentDTOList = new TreeSet<>();
        for (int i = 0; i < studentsTotal ; i++) {
            Student student = generateStudent(i);
            student.setName(wantedName);
            students.add(student);
            studentDTOList.add(student.toDTO());
        }

        BDDMockito.given(studentServiceMock.findAllByNameAsDTO(wantedName)).willReturn(studentDTOList);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + StudentController.READ_ALL_BY_NAME + "?name={name}", wantedName)
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, students);
    }

    @Test
    public void joinWork() throws Exception {
        final int workId = 3;
        final int studentId = 11;

        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Work> works = new TreeSet<>();

        final Student student = new Student(username, name, birthdate, 0, works);
        ReflectionTestUtils.setField(student, "id", studentId);
        BDDMockito.given(studentServiceMock.findById(student.getId())).willReturn(Optional.of(student));

        final String title = "Study of the year on Bee Movie";
        final String text = "According to all known laws\n" +
                "of aviation,\n" +
                "\n" +
                "  \n" +
                "there is no way a bee\n" +
                "should be able to fly.";

        final String author1username = "seinfeld";
        final String author1name = "Jerome Allen «Jerry» Seinfeld";
        int author1Id = 321;
        final Student author1 = new Student(author1username, author1name, birthdate, -1, works);
        ReflectionTestUtils.setField(author1, "id", author1Id);
        final Set<Student> authors = new TreeSet<>();
        authors.add(author1);

        Work workToJoin = new Work(title, text, authors, null);
        ReflectionTestUtils.setField(workToJoin, "id", workId);

        BDDMockito.doNothing().when(studentServiceMock).joinWork(studentId, workId);

        mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + StudentController.STUDENT_JOIN_WORK + "?studentId={studentId}&workId={workId}", studentId, workId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        ArgumentCaptor<Integer> studentIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> workIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(studentServiceMock, Mockito.times(1)).joinWork(studentIdCaptor.capture(), workIdCaptor.capture());
        Assertions.assertEquals(studentId, studentIdCaptor.getValue());
        Assertions.assertEquals(workId, workIdCaptor.getValue());
    }
}