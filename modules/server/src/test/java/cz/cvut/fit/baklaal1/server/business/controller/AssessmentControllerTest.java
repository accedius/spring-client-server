package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.entity.*;
import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import cz.cvut.fit.baklaal1.server.business.service.AssessmentService;
import cz.cvut.fit.baklaal1.server.business.service.helper.ServiceExceptionEntityAlreadyExists;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
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
@DisplayName("AssessmentController Test")
public class AssessmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssessmentService assessmentServiceMock;

    private static final String postAddress = AssessmentController.ASSESSMENT_DOMAIN_ROOT;

    @Test
    public void postConflict() throws Exception {
        final int assessmentId = 1;
        final AssessmentCreateDTO assessmentCreateDTO = generateAssessmentCreateDTO(assessmentId);

        BDDMockito.given(assessmentServiceMock.create(assessmentCreateDTO)).willThrow(new ServiceExceptionEntityAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateAssessmentCreateDTOJson(assessmentId)))

                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void postCreated() throws Exception {
        final int assessmentId = 1;
        final AssessmentCreateDTO assessmentCreateDTO = generateAssessmentCreateDTO(assessmentId);
        final AssessmentDTO assessmentDTO = generateAssessmentDTO(assessmentId);

        BDDMockito.given(assessmentServiceMock.create(assessmentCreateDTO)).willReturn(assessmentDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateAssessmentCreateDTOJson(assessmentId)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.endsWith(postAddress + "/" + assessmentId)));
    }

    @Test
    public void getOne() throws Exception {
        final int assessmentId = 1;
        final Assessment assessment = generateAssessment(assessmentId);

        BDDMockito.given(assessmentServiceMock.findById(assessmentId)).willReturn(java.util.Optional.of(assessment));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", assessmentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, assessment.toDTO());
    }

    @Test
    public void getOneNotFound() throws Exception {
        final int assessmentId = 1;

        BDDMockito.given(assessmentServiceMock.findById(assessmentId)).willReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", assessmentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOneLinks() throws Exception {
        final int assessmentId = 1;
        final Assessment assessment = generateAssessment(assessmentId);

        BDDMockito.given(assessmentServiceMock.findById(assessmentId)).willReturn(java.util.Optional.of(assessment));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", assessmentId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + assessmentId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
        checkResponse(result, assessment.toDTO());
    }

    @Test
    public void getPage() throws Exception {
        final int page = 1;
        final int size = 3;
        final int assessmentsTotal = 8;
        final Pageable pageable =  PageRequest.of(page, size);
        List<Assessment> assessments = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            Assessment assessment = generateAssessment(i);
            assessments.add(assessment);
        }
        final Page<Assessment> pageExpected = new PageImpl<>(assessments, pageable, assessmentsTotal);

        BDDMockito.given(assessmentServiceMock.pageAll(pageable)).willReturn(pageExpected);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "?page={page}&size={size}", page, size))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", CoreMatchers.containsString("page=2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", CoreMatchers.containsString("page=2")));

        checkDTOPage(result, assessments);
    }

    @Test
    public void getAll() throws Exception {
        final int assessmentsTotal = 8;
        List<Assessment> assessments = new ArrayList<>();
        for (int i = 0; i < assessmentsTotal ; i++) {
            Assessment assessment = generateAssessment(i);
            assessments.add(assessment);
        }

        BDDMockito.given(assessmentServiceMock.findAll()).willReturn(assessments);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/all")
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, assessments);
    }

    @Test
    public void update() throws Exception {
        final int assessmentId = 1;
        final AssessmentCreateDTO updatedAssessmentCreateDTO = generateAssessmentCreateDTO(assessmentId+1);
        final AssessmentDTO updatedAssessmentDTO = generateAssessmentDTO(assessmentId+1);
        setField(updatedAssessmentDTO, "id", assessmentId);

        BDDMockito.given(assessmentServiceMock.update(assessmentId, updatedAssessmentCreateDTO)).willReturn(updatedAssessmentDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + "/{id}", assessmentId)
                .contentType("application/json")
                .content(generateAssessmentCreateDTOJson(assessmentId+1)))

                .andExpect(MockMvcResultMatchers.status().isAccepted());
        checkResponse(result, updatedAssessmentDTO);
    }

    @Test
    public void delete() throws Exception {
        final int assessmentId = 1;

        BDDMockito.doNothing().when(assessmentServiceMock).delete(assessmentId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "/{id}", assessmentId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void readAllByEvaluatorId() throws Exception {
        final int assessmentsTotal = 8;
        final Integer evaluatorId = 1;
        List<Assessment> assessments = new ArrayList<>();
        Set<AssessmentDTO> assessmentDTOs = new TreeSet<>();

        Teacher teacher = new Teacher("username"+evaluatorId, "name"+evaluatorId, new Timestamp(1000000000*evaluatorId), 10000d*evaluatorId);
        ReflectionTestUtils.setField(teacher, "id", evaluatorId);

        for (int i = 0; i < assessmentsTotal ; i++) {
            Assessment assessment = generateAssessment(i);
            assessment.setEvaluator(teacher);
            assessments.add(assessment);
            AssessmentDTO assessmentDTO = assessment.toDTO();
            assessmentDTOs.add(assessmentDTO);
        }

        BDDMockito.given(assessmentServiceMock.findAllByEvaluatorIdAsDTO(evaluatorId)).willReturn(assessmentDTOs);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + AssessmentController.READ_ALL_BY_EVALUATOR_ID + "?evaluatorId={id}", evaluatorId)
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, assessments);
    }

    private int generateGrade(int i) {
        return Grades.A + i % Grades.F;
    }

    private Assessment generateAssessment(int i) {
        int grade = generateGrade(i);
        Work work = new Work("title"+i, "text"+i);
        ReflectionTestUtils.setField(work, "id", i);
        Teacher teacher = new Teacher("username"+i, "name"+i, new Timestamp(1000000000*i), 10000d*i);
        ReflectionTestUtils.setField(teacher, "id", i);
        Assessment assessment = new Assessment(grade, work, teacher);
        ReflectionTestUtils.setField(assessment, "id", i);
        return assessment;
    }

    private AssessmentDTO generateAssessmentDTO(int i) {
        return generateAssessment(i).toDTO();
    }

    private AssessmentCreateDTO generateAssessmentCreateDTO(int i) {
        return generateAssessment(i).toCreateDTO();
    }

    private void checkDTOPage(ResultActions resultToCheck, List<Assessment> assessments) throws Exception {
        final int size = assessments.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].id", CoreMatchers.is(assessments.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].grade", CoreMatchers.is(assessments.get(i).getGrade())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].workId", CoreMatchers.is(assessments.get(i).getWork().getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.assessmentDTOList[" + i + "].evaluatorId", CoreMatchers.is(assessments.get(i).getEvaluator().getId())));
        }
    }

    private void checkDTOList(ResultActions resultToCheck, List<Assessment> assessments) throws Exception {
        final int size = assessments.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(assessments.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].grade", CoreMatchers.is(assessments.get(i).getGrade())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].workId", CoreMatchers.is(assessments.get(i).getWork().getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].evaluatorId", CoreMatchers.is(assessments.get(i).getEvaluator().getId())));
        }
    }

    private <E, V> E setField(E entity, String fieldName, V fieldValue) {
        ReflectionTestUtils.setField(entity, fieldName, fieldValue);
        return entity;
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    private String generateAssessmentCreateDTOJson(int i) {
        String json = "{\"grade\" : \"" + generateGrade(i) + "\", \"workId\" : \"" + i + "\", \"evaluatorId\" : \"" + i + "\" }";
        return json;
    }

    private void checkLinks(ResultActions resultToCheck, AssessmentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    private void checkResponse(ResultActions resultToCheck, AssessmentDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade", CoreMatchers.is(checkAgainst.getGrade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workId", CoreMatchers.is(checkAgainst.getWorkId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.evaluatorId", CoreMatchers.is(checkAgainst.getEvaluatorId())));
        checkLinks(resultToCheck, checkAgainst);
    }
}
