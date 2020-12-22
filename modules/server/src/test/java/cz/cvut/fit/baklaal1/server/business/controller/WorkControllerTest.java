package cz.cvut.fit.baklaal1.server.business.controller;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.Work;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.server.business.service.WorkService;
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

import java.util.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@DisplayName("WorkController Test")
public class WorkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkService workServiceMock;

    private static final String postAddress = WorkController.WORK_DOMAIN_ROOT;

    @Test
    public void postConflict() throws Exception {
        final int workId = 1;
        final WorkCreateDTO workCreateDTO = generateWorkCreateDTO(workId);

        BDDMockito.given(workServiceMock.create(workCreateDTO)).willThrow(new ServiceExceptionEntityAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateWorkCreateDTOJson(workId)))

                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void postCreated() throws Exception {
        final int workId = 1;
        final WorkCreateDTO workCreateDTO = generateWorkCreateDTO(workId);
        final WorkDTO workDTO = generateWorkDTO(workId);

        BDDMockito.given(workServiceMock.create(workCreateDTO)).willReturn(workDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(postAddress)
                .contentType("application/json")
                .content(generateWorkCreateDTOJson(workId)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", CoreMatchers.endsWith(postAddress + "/" + workId)));
    }

    @Test
    public void getOne() throws Exception {
        final int workId = 1;
        final Work work = generateWork(workId);

        BDDMockito.given(workServiceMock.findById(workId)).willReturn(java.util.Optional.of(work));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", workId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk());
        checkResponse(result, work.toDTO());
    }

    @Test
    public void getOneNotFound() throws Exception {
        final int workId = 1;

        BDDMockito.given(workServiceMock.findById(workId)).willReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", workId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOneLinks() throws Exception {
        final int workId = 1;
        final Work work = generateWork(workId);

        BDDMockito.given(workServiceMock.findById(workId)).willReturn(java.util.Optional.of(work));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/{id}", workId)
                .accept("application/json")
                .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + workId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
        checkResponse(result, work.toDTO());
    }

    @Test
    public void getPage() throws Exception {
        final int page = 1;
        final int size = 3;
        final int worksTotal = 8;
        final Pageable pageable =  PageRequest.of(page, size);
        List<Work> works = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            Work work = generateWork(i);
            works.add(work);
        }
        final Page<Work> pageExpected = new PageImpl<>(works, pageable, worksTotal);

        BDDMockito.given(workServiceMock.pageAll(pageable)).willReturn(pageExpected);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "?page={page}&size={size}", page, size))

                .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", CoreMatchers.containsString("page=2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", CoreMatchers.containsString("page=2")));

        checkDTOPage(result, works);
    }

    @Test
    public void getAll() throws Exception {
        final int worksTotal = 8;
        List<Work> works = new ArrayList<>();
        for (int i = 0; i < worksTotal ; i++) {
            Work work = generateWork(i);
            works.add(work);
        }

        BDDMockito.given(workServiceMock.findAll()).willReturn(works);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + "/all")
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, works);
    }

    @Test
    public void update() throws Exception {
        final int workId = 1;
        final WorkCreateDTO updatedWorkCreateDTO = generateWorkCreateDTO(workId+1);
        final WorkDTO updatedWorkDTO = generateWorkDTO(workId+1);
        setField(updatedWorkDTO, "id", workId);

        BDDMockito.given(workServiceMock.update(workId, updatedWorkCreateDTO)).willReturn(updatedWorkDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(postAddress + "/{id}", workId)
                .contentType("application/json")
                .content(generateWorkCreateDTOJson(workId+1)))

                .andExpect(MockMvcResultMatchers.status().isAccepted());
        checkResponse(result, updatedWorkDTO);
    }

    @Test
    public void delete() throws Exception {
        final int workId = 1;

        BDDMockito.doNothing().when(workServiceMock).delete(workId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(postAddress + "/{id}", workId))

                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void readAllByTitle() throws Exception {
        final int worksTotal = 8;
        final String wantedTitle = "SpecificTitle";
        List<Work> works = new ArrayList<>();
        Set<WorkDTO> workDTOs = new TreeSet<>();
        for (int i = 0; i < worksTotal ; i++) {
            Work work = generateWork(i);
            setField(work, "title", wantedTitle);
            works.add(work);
            WorkDTO workDTO = work.toDTO();
            workDTOs.add(workDTO);
        }

        BDDMockito.given(workServiceMock.findAllByTitleAsDTO(wantedTitle)).willReturn(workDTOs);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(postAddress + WorkController.READ_ALL_BY_TITLE + "?title={title}", wantedTitle)
                .accept("application/json")
                .contentType("application/json"));
        checkDTOList(result, works);
    }

    private Work generateWork(int i) {
        String title = "title" + i;
        String text = "text" + i;
        Set<Student> authors = new TreeSet<>();
        Assessment assessment = null;
        Work work = new Work(title, text, authors, assessment);
        ReflectionTestUtils.setField(work, "id", i);
        return work;
    }

    private WorkDTO generateWorkDTO(int i) {
        return generateWork(i).toDTO();
    }

    private WorkCreateDTO generateWorkCreateDTO(int i) {
        return generateWork(i).toCreateDTO();
    }

    //TODO move to Jackson preferably, or to GSON or JSON-Java
    private String generateWorkCreateDTOJson(int i) {
        String json = "{\"title\" : \"title" + i + "\", \"text\" : \"text" + i + "\", \"authorIds\" : [], \"assessmentId\" : null }";
        return json;
    }

    private void checkLinks(ResultActions resultToCheck, WorkDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", CoreMatchers.endsWith(postAddress + "/" + checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.collection.href", CoreMatchers.containsString(postAddress)));
    }

    private void checkResponse(ResultActions resultToCheck, WorkDTO checkAgainst) throws Exception {
        resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(checkAgainst.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(checkAgainst.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(checkAgainst.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorIds", CoreMatchers.is(Matchers.empty())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assessmentId", CoreMatchers.is(checkAgainst.getAssessmentId())));
        checkLinks(resultToCheck, checkAgainst);
    }

    private void checkDTOPage(ResultActions resultToCheck, List<Work> works) throws Exception {
        final int size = works.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].id", CoreMatchers.is(works.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].title", CoreMatchers.is(works.get(i).getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].text", CoreMatchers.is(works.get(i).getText())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].authorIds", CoreMatchers.is(Matchers.empty())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.workDTOList[" + i + "].assessmentId", CoreMatchers.is(works.get(i).getAssessment())));
        }
    }

    private void checkDTOList(ResultActions resultToCheck, List<Work> works) throws Exception {
        final int size = works.size();
        for (int i = 0; i < size; i++) {
            resultToCheck.andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].id", CoreMatchers.is(works.get(i).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].title", CoreMatchers.is(works.get(i).getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].text", CoreMatchers.is(works.get(i).getText())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].authorIds", CoreMatchers.is(Matchers.empty())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].assessmentId", CoreMatchers.is(works.get(i).getAssessment())));
        }
    }

    private <E, V> E setField(E entity, String fieldName, V fieldValue) {
        ReflectionTestUtils.setField(entity, fieldName, fieldValue);
        return entity;
    }
}
