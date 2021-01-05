package cz.cvut.fit.baklaal1.model.data.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Set;

public class StudentDTO extends PersonDTO<StudentDTO> {
    private final float averageGrade;
    private final Set<Integer> workIds;

    @JsonCreator
    public StudentDTO(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("name") String name, @JsonProperty("birthdate") Timestamp birthdate, @JsonProperty("averageGrade") float averageGrade, @JsonProperty("workIds") Set<Integer> workIds) {
        super(id, username, name, birthdate);
        this.averageGrade = averageGrade;
        this.workIds = workIds;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public Set<Integer> getWorkIds() {
        return workIds;
    }

    @Override
    public void print() {
        System.out.println("Student: {");
        printFormatted("id", id);
        printFormatted("username", username);
        printFormatted("name", name);
        printFormatted("birthdate", birthdate);
        printFormatted("averageGrade", averageGrade);

        printCollectionFormatted("Work Ids", sortSet(workIds));

        printLinksFormatted(super.toString());
        System.out.println("}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentDTO)) return false;
        StudentDTO studentDTO = (StudentDTO) o;
        return super.equals(studentDTO) &&
                Float.compare(studentDTO.averageGrade, averageGrade) == 0 &&
                workIds.equals(studentDTO.workIds);
    }
}
