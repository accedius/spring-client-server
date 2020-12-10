package cz.cvut.fit.baklaal1.model.data.entity.dto;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class StudentCreateDTO extends PersonCreateDTO {
    private final float averageGrade;
    private final Set<Integer> workIds;

    public StudentCreateDTO(String username, String name, Timestamp birthDate, float averageGrade, Set<Integer> workIds) {
        super(username, name, birthDate);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentCreateDTO)) return false;
        if (!super.equals(o)) return false;
        StudentCreateDTO that = (StudentCreateDTO) o;
        return super.equals(o) && Float.compare(that.averageGrade, averageGrade) == 0 &&
                workIds.equals(that.workIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), averageGrade, workIds);
    }
}
