package cz.cvut.fit.baklaal1.model.data.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherDTO extends PersonDTO<TeacherDTO> {
    private final double wage;
    private final Set<Integer> assessmentIds;

    @JsonCreator
    public TeacherDTO(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("name") String name, @JsonProperty("birthdate") Timestamp birthdate, @JsonProperty("wage") double wage, @JsonProperty("assessmentIds") Set<Integer> assessmentIds) {
        super(id, username, name, birthdate);
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
    public void print() {
        System.out.println("Teacher: {");
        printFormatted("id", id);
        printFormatted("username", username);
        printFormatted("name", name);
        printFormatted("birthdate", birthdate);
        printFormatted("wage", wage);

        printCollectionFormatted("Assessment Ids", assessmentIds);

        printLinksFormatted(super.toString());
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
