package cz.cvut.fit.baklaal1.business.controller;

import cz.cvut.fit.baklaal1.business.service.StudentService;
import cz.cvut.fit.baklaal1.business.service.WorkService;
import cz.cvut.fit.baklaal1.business.service.helper.ServiceExceptionEntityAlreadyExists;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@DisplayName("StudentController Test")
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentServiceMock;

    private static final String postAddress = StudentController.STUDENT_DOMAIN_ROOT;

    @Test
    public void postConflict() throws Exception {
        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Integer> workIds = new TreeSet<>();

        final StudentCreateDTO studentCreateDTO = new StudentCreateDTO(username, name, birthdate, 0, workIds);
        BDDMockito.given(studentServiceMock.create(studentCreateDTO)).willThrow(new ServiceExceptionEntityAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content("{\"username\" : \"somebody\", \"name\" : \"Once Toldme\", \"birthdate\" : null, \"workIds\" : [] }"))

                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void postCreated() throws Exception {
        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Integer> workIds = new TreeSet<>();

        final StudentDTO studentDTO = new StudentDTO(1, username, name, birthdate, 0, workIds);
        final StudentCreateDTO studentCreateDTO = new StudentCreateDTO(username, name, birthdate, 0, workIds);
        BDDMockito.given(studentServiceMock.create(studentCreateDTO)).willReturn(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content("{\"username\" : \"somebody\", \"name\" : \"Once Toldme\", \"birthdate\" : null, \"workIds\" : [] }"))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.endsWith(postAddress + "/" + studentDTO.getId())));
    }

    @Test
    public void getOne() throws Exception {
        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Work> works = new TreeSet<>();

        final Student student = new Student(username, name, birthdate, 0, works);
        ReflectionTestUtils.setField(student, "id", 1);
        BDDMockito.given(studentServiceMock.findById(student.getId())).willReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", student.getId())
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(student.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(student.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate", CoreMatchers.is(student.getBirthdate())))
                //TODO if uses student{DTO}.getWorkIds() then java.lang.AssertionError: JSON path "$.workIds"
                //     Expected: is <[]>
                //     but: was <[]>
                .andExpect(MockMvcResultMatchers.jsonPath("$.workIds", CoreMatchers.is(Matchers.empty())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getOneNotFound() throws Exception {
        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Integer> workIds = new TreeSet<>();

        final StudentDTO studentDTO = new StudentDTO(1, username, name, birthdate, 0, workIds);
        final StudentCreateDTO studentCreateDTO = new StudentCreateDTO(username, name, birthdate, 0, workIds);
        BDDMockito.given(studentServiceMock.findByIdAsDTO(studentDTO.getId())).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", studentDTO.getId())
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOneLinks() throws Exception {
        final String username = "somebody";
        final String name = "Once Toldme";
        final Timestamp birthdate = null;
        final Set<Work> works = new TreeSet<>();

        final Student student = new Student(username, name, birthdate, 0, works);
        ReflectionTestUtils.setField(student, "id", 1);
        BDDMockito.given(studentServiceMock.findById(student.getId())).willReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", student.getId())
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + student.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getPage() throws Exception {
        final int page = 0;
        final int size = 3;
        final int total = 8;
        final Pageable pageable =  PageRequest.of(page, size);
        final List<Student> data = List.of(
                new Student("user1", "Shrek", new Timestamp(1337), 2f),
                new Student("user2", "Donkey", new Timestamp(420), 2.666f),
                new Student("user3", "Fiona", new Timestamp(1234), 1f));
        final Page<Student> pageExpected = new PageImpl<>(data, pageable, total);

        BDDMockito.given(studentServiceMock.pageAll(pageable)).willReturn(pageExpected);

        mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "?page={page}&size={size}", page, size))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", CoreMatchers.containsString("page=1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", CoreMatchers.containsString("page=2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[0].username", CoreMatchers.is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[0].averageGrade", CoreMatchers.is(Double.valueOf(Float.toString(data.get(0).getAverageGrade())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[1].username", CoreMatchers.is("user2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[1].averageGrade", CoreMatchers.is(Double.valueOf(Float.toString(data.get(1).getAverageGrade())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[2].username", CoreMatchers.is("user3")));
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

        /*BDDMockito.given(workServiceMock.findById(workId)).willReturn(Optional.of(workToJoin));
        BDDMockito.given(studentServiceMock.findById(studentId)).willReturn(Optional.of(student));
        BDDMockito.given(workServiceMock.update(any(Integer.class), any(WorkCreateDTO.class))).willReturn(workToJoin.toDTO());

        mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + StudentController.STUDENT_JOIN_WORK + "?studentId={studentId}&workId={workId}", studentId, workId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        ArgumentCaptor<WorkCreateDTO> workCreateDTOArgumentCaptor = ArgumentCaptor.forClass(WorkCreateDTO.class);
        ArgumentCaptor<Integer> idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        Mockito.verify(workServiceMock, Mockito.atLeastOnce()).findById(idArgumentCaptor.capture());
        Assertions.assertEquals(workId, idArgumentCaptor.getValue());

        Mockito.verify(studentServiceMock, Mockito.atLeastOnce()).findById(idArgumentCaptor.capture());
        Assertions.assertEquals(studentId, idArgumentCaptor.getValue());

        WorkCreateDTO workJoinedByStudentCreateDTO = workToJoin.toCreateDTO();
        workJoinedByStudentCreateDTO.getAuthorIds().add(studentId);

        Mockito.verify(workServiceMock, Mockito.atLeastOnce()).update(idArgumentCaptor.capture(), workCreateDTOArgumentCaptor.capture());
        Assertions.assertEquals(workId, idArgumentCaptor.getValue());
        WorkCreateDTO providedWorkJoinedByStudentCreateDTO = workCreateDTOArgumentCaptor.getValue();
        Assertions.assertEquals(workJoinedByStudentCreateDTO, providedWorkJoinedByStudentCreateDTO);*/
    }
}