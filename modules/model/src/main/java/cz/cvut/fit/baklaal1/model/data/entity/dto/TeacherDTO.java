package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Teacher;

import java.sql.Timestamp;
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

    public double getWage() {
        return wage;
    }

    public Set<Integer> getAssessmentIds() {
        return assessmentIds;
    }

    @Override
    public void print() {
        System.out.println("Teacher: {");
        printFormatted("id", id);
        printFormatted("username", username);
        printFormatted("name", name);
        printFormatted("birthdate", birthdate);
        printFormatted("wage", wage);

        //TODO make a universal method to print collections formatted in json-like format
        System.out.println("Assessment Ids: {");
        for (Integer assessmentId : assessmentIds) {
            printFormatted("assessmentId", assessmentId);
        }
        System.out.println("}");

        System.out.println(super.toString());
        System.out.println("}");
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
