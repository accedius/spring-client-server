package cz.cvut.fit.baklaal1.server.suite;

import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.server.business.controller.StudentController;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;

public class StudentTestSuite extends BasicTestSuite {
    protected static final String postAddress = StudentController.STUDENT_DOMAIN_ROOT;

    protected <L extends Collection<Student>> L fillStudentCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateStudent(i));
        }
        return collection;
    }

    protected StudentDTO generateStudentDTO(int i) {
        return generateStudent(i).toDTO();
    }

    protected <L extends Collection<StudentDTO>> L fillStudentDTOCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateStudentDTO(i));
        }
        return collection;
    }

    protected StudentCreateDTO generateStudentCreateDTO(int i) {
        return generateStudent(i).toCreateDTO();
    }
    
    protected void checkDTOPage(ResultActions resultToCheck, List<Student> students) throws Exception {
        final int size = students.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].id", CoreMatchers.is(students.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].username", CoreMatchers.is(students.get(i).getUsername())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].name", CoreMatchers.is(students.get(i).getName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].birthdate", CoreMatchers.is(students.get(i).getBirthdate())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].averageGrade", CoreMatchers.is(Double.valueOf(Float.toString(students.get(i).getAverageGrade())))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.studentDTOList[" + i + "].workIds", CoreMatchers.is(Matchers.empty())));
        }
    }

    protected void checkDTOList(ResultActions resultToCheck, List<Student> students) throws Exception {
        final int size = students.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(students.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].username", CoreMatchers.is(students.get(i).getUsername())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].name", CoreMatchers.is(students.get(i).getName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].birthdate", CoreMatchers.is(students.get(i).getBirthdate())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].averageGrade", CoreMatchers.is(Double.valueOf(Float.toString(students.get(i).getAverageGrade())))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].workIds", CoreMatchers.is(Matchers.empty())));
        }
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    protected String generateStudentCreateDTOJson(int i) {
        String json = "{\"username\" : \"" + generateUsername("student", i) + "\", \"name\" : \"studentName" + i + "\", \"birthdate\" : \"null\", \"averageGrade\" : \"" + generateAverageGrade(i) +"\", \"workIds\" : [] }";
        return json;
    }

    protected void checkLinks(ResultActions resultToCheck, StudentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    protected void checkResponse(ResultActions resultToCheck, StudentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(checkAgainst.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(checkAgainst.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate", CoreMatchers.is(checkAgainst.getBirthdate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.averageGrade", CoreMatchers.is(Double.valueOf(Float.toString(checkAgainst.getAverageGrade())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workIds", CoreMatchers.is(Matchers.empty())));
        checkLinks(resultToCheck, checkAgainst);
    }
}
