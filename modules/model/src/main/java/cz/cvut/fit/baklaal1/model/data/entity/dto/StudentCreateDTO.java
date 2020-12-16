package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.helper.Grades;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class StudentCreateDTO extends PersonCreateDTO {
    private static final Set<Integer> defaultWorkIds = new TreeSet<>();

    private float averageGrade;
    private Set<Integer> workIds;

    public StudentCreateDTO(String username, String name, Timestamp birthDate, float averageGrade, Set<Integer> workIds) {
        super(username, name, birthDate);
        this.averageGrade = averageGrade;
        this.workIds = workIds;
    }

    public void setAverageGrade(float averageGrade) {
        if(Grades.isGrade(averageGrade)) {
            this.averageGrade = averageGrade;
        } else {
            this.averageGrade = Grades.DEFAULT;
        }
    }

    public void setWorkIds(Set<Integer> workIds) {
        this.workIds = Objects.requireNonNullElseGet(workIds, this::getDefaultWorkIds);
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

    private Set<Integer> getDefaultWorkIds() {
        return new TreeSet<>(defaultWorkIds);
    }
}
