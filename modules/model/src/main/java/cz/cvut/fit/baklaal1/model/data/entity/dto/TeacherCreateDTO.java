package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Teacher;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherCreateDTO extends PersonCreateDTO {
    private double wage;
    private Set<Integer> assessmentIds;

    public TeacherCreateDTO(String username, String name, Timestamp birthDate, double wage, Set<Integer> assessmentIds) {
        super(username, name, birthDate);
        this.wage = wage;
        this.assessmentIds = assessmentIds;
    }

    public TeacherCreateDTO(Teacher teacher) {
        super(teacher.getUsername(), teacher.getName(), teacher.getBirthdate());
        this.wage = teacher.getWage();
        Set<Assessment> assessments = teacher.getAssessments();
        this.assessmentIds = assessments.stream().map(Assessment::getId).collect(Collectors.toSet());
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
