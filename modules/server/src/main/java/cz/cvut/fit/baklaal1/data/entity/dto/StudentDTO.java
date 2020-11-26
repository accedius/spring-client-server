package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Person;
import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDTO extends PersonDTO {
    private final float averageGrade;
    private final List<Integer> workIds;

    public StudentDTO(int id, String username, String name, Timestamp birthDate, float averageGrade, List<Integer> workIds) {
        super(id, username, name, birthDate);
        this.averageGrade = averageGrade;
        this.workIds = workIds;
    }

    public StudentDTO(Student student) {
        super(student);
        this.averageGrade = student.getAverageGrade();
        List<Work> works = student.getWorks();
        this.workIds = works.stream().map(Work::getId).collect(Collectors.toList());
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public List<Integer> getWorkIds() {
        return workIds;
    }
}
