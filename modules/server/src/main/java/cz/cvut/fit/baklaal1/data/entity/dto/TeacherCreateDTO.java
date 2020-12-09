package cz.cvut.fit.baklaal1.data.entity.dto;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class TeacherCreateDTO extends PersonCreateDTO {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherCreateDTO)) return false;
        if (!super.equals(o)) return false;
        TeacherCreateDTO that = (TeacherCreateDTO) o;
        return super.equals(o) && Double.compare(that.wage, wage) == 0 &&
                assessmentIds.equals(that.assessmentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), wage, assessmentIds);
    }
}
