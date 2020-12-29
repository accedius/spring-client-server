package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.helper.Grades;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StudentCreateDTO extends PersonCreateDTO {
    private static final Set<Integer> defaultWorkIds = new TreeSet<>();

    private float averageGrade;
    private Set<Integer> workIds;

    public StudentCreateDTO(String username, String name, Timestamp birthdate, float averageGrade, Set<Integer> workIds) {
        super(username, name, birthdate);
        this.averageGrade = makeAverageGrade(averageGrade);
        this.workIds = makeWorkIds(workIds);
    }

    public StudentCreateDTO() {
    }

    private float makeAverageGrade(float averageGrade) {
        if(Grades.isGradeDerived(averageGrade)) {
            return averageGrade;
        } else {
            return Grades.DEFAULT;
        }
    }

    private Set<Integer> makeWorkIds(Set<Integer> workIds) {
        return Objects.requireNonNullElseGet(workIds, this::getDefaultWorkIds);
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
