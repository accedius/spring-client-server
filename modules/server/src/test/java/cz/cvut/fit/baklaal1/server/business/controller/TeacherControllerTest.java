package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.entity.Teacher;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.server.business.service.TeacherService;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionEntityAlreadyExists;
import cz.cvut.fit.baklaal1.server.suite.TeacherTestSuite;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@DisplayName("TeacherController Test")
public class TeacherControllerTest extends TeacherTestSuite {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherServiceMock;

    @Test
    public void postConflict() throws Exception {
        final int teacherId = 1;
        final TeacherCreateDTO teacherCreateDTO = generateTeacherCreateDTO(teacherId);

        BDDMockito.given(teacherServiceMock.create(teacherCreateDTO)).willThrow(new ServiceExceptionEntityAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateTeacherCreateDTOJson(teacherId)))

                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void postCreated() throws Exception {
        final int teacherId = 1;
        final TeacherCreateDTO teacherCreateDTO = generateTeacherCreateDTO(teacherId);
        final TeacherDTO teacherDTO = generateTeacherDTO(teacherId);

        BDDMockito.given(teacherServiceMock.create(teacherCreateDTO)).willReturn(teacherDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateTeacherCreateDTOJson(teacherId)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.endsWith(postAddress + "/" + teacherId)));
    }

    @Test
    public void getOne() throws Exception {
        final int teacherId = 1;
        final Teacher teacher = generateTeacher(teacherId);

        BDDMockito.given(teacherServiceMock.findById(teacherId)).willReturn(Optional.of(teacher));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", teacherId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, teacher.toDTO());
    }

    @Test
    public void getOneNotFound() throws Exception {
        final int teacherId = 1;

        BDDMockito.given(teacherServiceMock.findById(teacherId)).willReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", teacherId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOneLinks() throws Exception {
        final int teacherId = 1;
        final Teacher teacher = generateTeacher(teacherId);

        BDDMockito.given(teacherServiceMock.findById(teacherId)).willReturn(Optional.of(teacher));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", teacherId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + teacherId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
        checkResponse(result, teacher.toDTO());
    }

    @Test
    public void getPage() throws Exception {
        final int page = 1;
        final int size = 3;
        final int teachersTotal = 8;
        final Pageable pageable =  PageRequest.of(page, size);
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            Teacher teacher = generateTeacher(i);
            teachers.add(teacher);
        }
        final Page<Teacher> pageExpected = new PageImpl<>(teachers, pageable, teachersTotal);

        BDDMockito.given(teacherServiceMock.pageAll(pageable)).willReturn(pageExpected);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "?page={page}&size={size}", page, size))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", CoreMatchers.containsString("page=2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", CoreMatchers.containsString("page=2")));

        checkDTOPage(result, teachers);
    }

    @Test
    public void getAll() throws Exception {
        final int teachersTotal = 8;
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 0; i < teachersTotal ; i++) {
            Teacher teacher = generateTeacher(i);
            teachers.add(teacher);
        }

        BDDMockito.given(teacherServiceMock.findAll()).willReturn(teachers);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/all")
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, teachers);
    }

    @Test
    public void update() throws Exception {
        final int teacherId = 1;
        final TeacherCreateDTO updatedTeacherCreateDTO = generateTeacherCreateDTO(teacherId+1);
        final TeacherDTO updatedTeacherDTO = generateTeacherDTO(teacherId+1);
        setField(updatedTeacherDTO, "id", teacherId);

        BDDMockito.given(teacherServiceMock.update(teacherId, updatedTeacherCreateDTO)).willReturn(updatedTeacherDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + "/{id}", teacherId)
                .contentType("application/json")
                .content(generateTeacherCreateDTOJson(teacherId+1)))

                .andExpect(MockMvcResultMatchers.status().isAccepted());
        checkResponse(result, updatedTeacherDTO);
    }

    @Test
    public void delete() throws Exception {
        final int teacherId = 1;

        BDDMockito.doNothing().when(teacherServiceMock).delete(teacherId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "/{id}", teacherId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteByUsername() throws Exception {
        final String username = "username";

        BDDMockito.doNothing().when(teacherServiceMock).deleteByUsername(username);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "?username={username}", username))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void readByUsername() throws Exception {
        final int teacherId = 1;
        final Teacher teacher = generateTeacher(teacherId);

        BDDMockito.given(teacherServiceMock.findByUsernameAsDTO(teacher.getUsername())).willReturn(Optional.of(teacher.toDTO()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + TeacherController.READ_BY_USERNAME + "?username={username}", teacher.getUsername())
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, teacher.toDTO());
    }

    @Test
    public void readAllByName() throws Exception {
        final int teachersTotal = 8;
        final String wantedName = "SpecificName";
        List<Teacher> teachers = new ArrayList<>();
        Set<TeacherDTO> teacherDTOList = new TreeSet<>();
        for (int i = 0; i < teachersTotal ; i++) {
            Teacher teacher = generateTeacher(i);
            teacher.setName(wantedName);
            teachers.add(teacher);
            teacherDTOList.add(teacher.toDTO());
        }

        BDDMockito.given(teacherServiceMock.findAllByNameAsDTO(wantedName)).willReturn(teacherDTOList);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + TeacherController.READ_ALL_BY_NAME + "?name={name}", wantedName)
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, teachers);
    }
}
