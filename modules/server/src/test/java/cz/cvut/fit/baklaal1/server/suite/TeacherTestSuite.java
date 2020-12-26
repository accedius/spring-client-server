package cz.cvut.fit.baklaal1.server.suite;

import cz.cvut.fit.baklaal1.entity.Teacher;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.server.business.controller.TeacherController;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;

public class TeacherTestSuite extends BasicTestSuite {
    protected static final String postAddress = TeacherController.TEACHER_DOMAIN_ROOT;

    protected <L extends Collection<Teacher>> L fillTeacherCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateTeacher(i));
        }
        return collection;
    }

    protected TeacherDTO generateTeacherDTO(int i) {
        return generateTeacher(i).toDTO();
    }

    protected <L extends Collection<TeacherDTO>> L fillTeacherDTOCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateTeacherDTO(i));
        }
        return collection;
    }

    protected TeacherCreateDTO generateTeacherCreateDTO(int i) {
        return generateTeacher(i).toCreateDTO();
    }

    protected void checkDTOPage(ResultActions resultToCheck, List<Teacher> teachers) throws Exception {
        final int size = teachers.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].id", CoreMatchers.is(teachers.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].username", CoreMatchers.is(teachers.get(i).getUsername())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].name", CoreMatchers.is(teachers.get(i).getName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].birthdate", CoreMatchers.is(teachers.get(i).getBirthdate())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].wage", CoreMatchers.is(teachers.get(i).getWage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.teacherDTOList[" + i + "].assessmentIds", CoreMatchers.is(Matchers.empty())));
        }
    }

    protected void checkDTOList(ResultActions resultToCheck, List<Teacher> teachers) throws Exception {
        final int size = teachers.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(teachers.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].username", CoreMatchers.is(teachers.get(i).getUsername())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].name", CoreMatchers.is(teachers.get(i).getName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].birthdate", CoreMatchers.is(teachers.get(i).getBirthdate())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].wage", CoreMatchers.is(teachers.get(i).getWage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].assessmentIds", CoreMatchers.is(Matchers.empty())));
        }
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    protected String generateTeacherCreateDTOJson(int i) {
        String json = "{\"username\" : \"username" + i + "\", \"name\" : \"name" + i + "\", \"birthdate\" : \"null\", \"wage\" : \"" + 10000d*i +"\", \"assessmentIds\" : [] }";
        return json;
    }

    protected void checkLinks(ResultActions resultToCheck, TeacherDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    protected void checkResponse(ResultActions resultToCheck, TeacherDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(checkAgainst.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(checkAgainst.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate", CoreMatchers.is(checkAgainst.getBirthdate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wage", CoreMatchers.is(checkAgainst.getWage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assessmentIds", CoreMatchers.is(Matchers.empty())));
        checkLinks(resultToCheck, checkAgainst);
    }
}
