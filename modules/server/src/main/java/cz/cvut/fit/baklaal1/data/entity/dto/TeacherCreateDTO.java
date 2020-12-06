package cz.cvut.fit.baklaal1.data.entity.dto;

import java.sql.Timestamp;
import java.util.Set;

public class TeacherCreateDTO extends PersonCreateDTO{
    private double wage;
    private Set<Integer> assessmentIds;

    public TeacherCreateDTO(String username, String name, Timestamp birthDate, double wage, Set<Integer> assessmentIds) {
        super(username, name, birthDate);
        this.wage = wage;
        this.assessmentIds = assessmentIds;
    }

    public double getWage() {
        return wage;
    }

    public Set<Integer> getAssessmentIds() {
        return assessmentIds;
    }
}
