package cz.cvut.fit.baklaal1.data.entity.dto;

import java.sql.Timestamp;
import java.util.List;

public class StudentCreateDTO extends PersonCreateDTO {
    private final float averageGrade;
    private final List<Integer> workIds;

    public StudentCreateDTO(String username, String name, Timestamp birthDate, float averageGrade, List<Integer> workIds) {
        super(username, name, birthDate);
        this.averageGrade = averageGrade;
        this.workIds = workIds;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public List<Integer> getWorkIds() {
        return workIds;
    }
}
