package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.Teacher;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherDTO extends PersonDTO {
    private double wage;
    private List<Integer> assessmentIds;

    public TeacherDTO(int id, String username, String name, Timestamp birthDate, double wage, List<Integer> assessmentIds) {
        super(id, username, name, birthDate);
        this.wage = wage;
        this.assessmentIds = assessmentIds;
    }

    public TeacherDTO(Teacher teacher) {
        super(teacher);
        this.wage = teacher.getWage();
        List<Assessment> assessments = teacher.getAssessments();
        this.assessmentIds = assessments.stream().map(Assessment::getId).collect(Collectors.toList());
    }

    public double getWage() {
        return wage;
    }

    public List<Integer> getAssessmentIds() {
        return assessmentIds;
    }
}
