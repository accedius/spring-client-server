package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Teacher;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherDTO extends PersonDTO<TeacherDTO> {
    private double wage;
    private Set<Integer> assessmentIds;

    public TeacherDTO(int id, String username, String name, Timestamp birthDate, double wage, Set<Integer> assessmentIds) {
        super(id, username, name, birthDate);
        this.wage = wage;
        this.assessmentIds = assessmentIds;
    }

    public TeacherDTO(final Teacher teacher) {
        super(teacher);
        this.wage = teacher.getWage();
        Set<Assessment> assessments = teacher.getAssessments();
        this.assessmentIds = assessments.stream().map(Assessment::getId).collect(Collectors.toSet());
    }

    @Override
    public int readId() {
        return getId();
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
        if (!(o instanceof TeacherDTO)) return false;
        TeacherDTO teacherDTO = (TeacherDTO) o;
        return super.equals(teacherDTO) &&
                Double.compare(teacherDTO.wage, wage) == 0 &&
                assessmentIds.equals(teacherDTO.assessmentIds);
    }
}
