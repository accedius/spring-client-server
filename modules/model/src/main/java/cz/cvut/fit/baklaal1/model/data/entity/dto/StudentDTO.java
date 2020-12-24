package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.entity.Student;
import cz.cvut.fit.baklaal1.model.data.entity.Work;

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

    public StudentDTO(final Student student) {
        super(student);
        this.averageGrade = student.getAverageGrade();
        Set<Work> works = student.getWorks();
        this.workIds = works.stream().map(Work::getId).collect(Collectors.toSet());
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

        //TODO make a universal method to print collections formatted in json-like format
        System.out.println("Work Ids: {");
        for (Integer workId : workIds) {
            printFormatted("workId", workId);
        }
        System.out.println("}");

        System.out.println(super.toString());
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
