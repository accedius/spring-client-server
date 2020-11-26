package cz.cvut.fit.baklaal1.data.entity.dto;

import java.sql.Timestamp;
import java.util.List;

public class TeacherCreateDTO extends PersonCreateDTO{
    private double wage;
    private List<Integer> assessmentIds;

    public TeacherCreateDTO(String username, String name, Timestamp birthDate, double wage, List<Integer> assessmentIds) {
        super(username, name, birthDate);
        this.wage = wage;
        this.assessmentIds = assessmentIds;
    }

    public double getWage() {
        return wage;
    }

    public List<Integer> getAssessmentIds() {
        return assessmentIds;
    }
}
