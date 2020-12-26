package cz.cvut.fit.baklaal1.server.suite;

import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.server.business.controller.AssessmentController;
import org.hamcrest.CoreMatchers;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;

public class AssessmentTestSuite extends BasicTestSuite {
    protected static final String postAddress = AssessmentController.ASSESSMENT_DOMAIN_ROOT;

    protected <L extends Collection<Assessment>> L fillAssessmentCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateAssessment(i));
        }
        return collection;
    }

    protected AssessmentDTO generateAssessmentDTO(int i) {
        return generateAssessment(i).toDTO();
    }

    protected <L extends Collection<AssessmentDTO>> L fillAssessmentDTOCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateAssessmentDTO(i));
        }
        return collection;
    }

    protected AssessmentCreateDTO generateAssessmentCreateDTO(int i) {
        return generateAssessment(i).toCreateDTO();
    }

    protected void checkDTOPage(ResultActions resultToCheck, List<Assessment> assessments) throws Exception {
        final int size = assessments.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].id", CoreMatchers.is(assessments.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].grade", CoreMatchers.is(assessments.get(i).getGrade())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].workId", CoreMatchers.is(assessments.get(i).getWork().getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].evaluatorId", CoreMatchers.is(assessments.get(i).getEvaluator().getId())));
        }
    }

    protected void checkDTOList(ResultActions resultToCheck, List<Assessment> assessments) throws Exception {
        final int size = assessments.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(assessments.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].grade", CoreMatchers.is(assessments.get(i).getGrade())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].workId", CoreMatchers.is(assessments.get(i).getWork().getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].evaluatorId", CoreMatchers.is(assessments.get(i).getEvaluator().getId())));
        }
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    protected String generateAssessmentCreateDTOJson(int i) {
        String json = "{\"grade\" : \"" + generateGrade(i) + "\", \"workId\" : \"" + i + "\", \"evaluatorId\" : \"" + i + "\" }";
        return json;
    }

    protected void checkLinks(ResultActions resultToCheck, AssessmentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    protected void checkResponse(ResultActions resultToCheck, AssessmentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade", CoreMatchers.is(checkAgainst.getGrade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workId", CoreMatchers.is(checkAgainst.getWorkId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.evaluatorId", CoreMatchers.is(checkAgainst.getEvaluatorId())));
        checkLinks(resultToCheck, checkAgainst);
    }
}
