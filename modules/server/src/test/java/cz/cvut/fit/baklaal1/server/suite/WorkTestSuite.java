package cz.cvut.fit.baklaal1.server.suite;

import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.server.business.controller.WorkController;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;

public class WorkTestSuite extends BasicTestSuite {
    protected static final String postAddress = WorkController.WORK_DOMAIN_ROOT;
    
    protected <L extends Collection<Work>> L fillWorkCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateWork(i));
        }
        return collection;
    }

    protected WorkDTO generateWorkDTO(int i) {
        return generateWork(i).toDTO();
    }

    protected <L extends Collection<WorkDTO>> L fillWorkDTOCollection(L collection, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(generateWorkDTO(i));
        }
        return collection;
    }

    protected WorkCreateDTO generateWorkCreateDTO(int i) {
        return generateWork(i).toCreateDTO();
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    protected String generateWorkCreateDTOJson(int i) {
        String json = "{\"title\" : \"" + generateTitle(i) + "\", \"text\" : \"text" + i + "\", \"authorIds\" : [], \"assessmentId\" : null }";
        return json;
    }

    protected void checkLinks(ResultActions resultToCheck, WorkDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    protected void checkResponse(ResultActions resultToCheck, WorkDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(checkAgainst.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(checkAgainst.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorIds", CoreMatchers.is(Matchers.empty())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assessmentId", CoreMatchers.is(checkAgainst.getAssessmentId())));
        checkLinks(resultToCheck, checkAgainst);
    }

    protected void checkDTOPage(ResultActions resultToCheck, List<Work> works) throws Exception {
        final int size = works.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].id", CoreMatchers.is(works.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].title", CoreMatchers.is(works.get(i).getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].text", CoreMatchers.is(works.get(i).getText())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].authorIds", CoreMatchers.is(Matchers.empty())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].assessmentId", CoreMatchers.is(works.get(i).getAssessment())));
        }
    }

    protected void checkDTOList(ResultActions resultToCheck, List<Work> works) throws Exception {
        final int size = works.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(works.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].title", CoreMatchers.is(works.get(i).getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].text", CoreMatchers.is(works.get(i).getText())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].authorIds", CoreMatchers.is(Matchers.empty())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].assessmentId", CoreMatchers.is(works.get(i).getAssessment())));
        }
    }
}
