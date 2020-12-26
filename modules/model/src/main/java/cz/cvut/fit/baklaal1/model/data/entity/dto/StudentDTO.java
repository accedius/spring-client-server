package cz.cvut.fit.baklaal1.model.data.entity.dto;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentDTO extends PersonDTO<StudentDTO> {
    private final float averageGrade;
    private final Set<Integer> workIds;

    public StudentDTO(int id, String username, String name, Timestamp birthDate, float averageGrade, Set<Integer> workIds) {
        super(id, username, name, birthDate);
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

        printCollectionFormatted("Work Ids", workIds);

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
