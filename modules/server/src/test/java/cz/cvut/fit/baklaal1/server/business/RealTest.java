package cz.cvut.fit.baklaal1.server.business;

import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import cz.cvut.fit.baklaal1.server.business.service.StudentService;
import cz.cvut.fit.baklaal1.server.business.service.WorkService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//TODO maybe should just use one DataSource for all the Test classes, since Spring creates HikariDataSource pool for each Test class in runtime, causing opening and closing same database Connection for each Test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Real Database Test")
class RealTest {
    @Autowired
    private StudentService studentService;

    @Autowired
    private WorkService workService;

    private static Set<Integer> studentIds = new TreeSet<>();
    private static Set<Integer> workIds = new TreeSet<>();

    private static StudentService studentStaticService;
    private static WorkService workStaticService;

    private Student generateStudent(int i) {
        String username = "studentUsername" + studentService.hashCode() + "_" + i;
        String name = "studentName" + i;
        Timestamp birthdate = new Timestamp(i*10000000);
        float averageGrade = (i * 0.1f) % Grades.E + Grades.A;
        Student student = new Student(username, name, birthdate, averageGrade);
        ReflectionTestUtils.setField(student, "id", i);
        return student;
    }

    private StudentDTO generateStudentDTO(int i) {
        return generateStudent(i).toDTO();
    }

    private StudentCreateDTO generateStudentCreateDTO(int i) {
        return generateStudent(i).toCreateDTO();
    }

    private WorkCreateDTO generateWorkCreateDTO(int i) {
        String title = "title" + workService.hashCode() + "_" + i;
        String text = "text"+i;
        Set<Integer> authorIds = new TreeSet<>();
        Integer assessmentId = null;
        return new WorkCreateDTO(title, text, authorIds, assessmentId);
    }

    @Test
    public void testOnRealDatabase() throws Exception {
        //TODO rework to initialize
        studentStaticService = studentService;
        workStaticService = workService;

        StudentCreateDTO myStudentToCreate1 = generateStudentCreateDTO(1);
        StudentCreateDTO myStudentToCreate2 = generateStudentCreateDTO(2);

        StudentDTO myStudentDTO1 = studentService.create(myStudentToCreate1);
        int studentId1 = myStudentDTO1.getId();
        studentIds.add(studentId1);
        Student myStudent1 = generateStudent(1);
        ReflectionTestUtils.setField(myStudent1, "id", studentId1);
        assertEquals(myStudent1.toDTO(), myStudentDTO1);

        StudentDTO myStudentDTO2 = studentService.create(myStudentToCreate2);
        int studentId2 = myStudentDTO2.getId();
        studentIds.add(studentId2);
        Student myStudent2 = generateStudent(2);
        ReflectionTestUtils.setField(myStudent2, "id", studentId2);
        assertEquals(myStudent2.toDTO(), myStudentDTO2);

        WorkCreateDTO myWorkToCreate = generateWorkCreateDTO(1);
        myWorkToCreate.getAuthorIds().add(studentId1);
        WorkDTO myWorkDTO = workService.create(myWorkToCreate);
        Integer workId = myWorkDTO.getId();
        workIds.add(workId);

        assertEquals(myWorkToCreate.getTitle(), myWorkDTO.getTitle());
        assertEquals(myWorkToCreate.getText(), myWorkDTO.getText());
        assertNull(myWorkDTO.getAssessmentId());
        assertTrue(myWorkDTO.getAuthorIds().contains(studentId1));

        studentService.joinWork(studentId2, workId);

        myWorkDTO = workService.findByIdAsDTO(workId).orElseThrow();
        assertEquals(myWorkToCreate.getTitle(), myWorkDTO.getTitle());
        assertEquals(myWorkToCreate.getText(), myWorkDTO.getText());
        assertNull(myWorkDTO.getAssessmentId());
        assertTrue(myWorkDTO.getAuthorIds().contains(studentId1));
        assertTrue(myWorkDTO.getAuthorIds().contains(studentId2));


        myWorkToCreate.getAuthorIds().remove(studentId1);
        myWorkToCreate.getAuthorIds().add(studentId2);
        myWorkDTO = workService.update(workId, myWorkToCreate);
        assertFalse(myWorkDTO.getAuthorIds().contains(studentId1));

        studentService.deleteByUsername(myStudent1.getUsername());
        assertFalse(studentService.findById(studentId1).isPresent());
        studentIds.remove(studentId1);

        myWorkDTO = workService.findByIdAsDTO(workId).orElseThrow();
        assertFalse(myWorkDTO.getAuthorIds().contains(studentId1));
        assertTrue(myWorkDTO.getAuthorIds().contains(studentId2));
    }

    @AfterAll
    static void afterAll() {
        for(Integer id : workIds) {
            workStaticService.delete(id);
        }
        for(Integer id : studentIds) {
            studentStaticService.delete(id);
        }
    }
}