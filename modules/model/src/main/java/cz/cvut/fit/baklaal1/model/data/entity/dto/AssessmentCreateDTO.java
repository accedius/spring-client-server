package cz.cvut.fit.baklaal1.model.data.entity.dto;

import java.util.Objects;

public class AssessmentCreateDTO {
    private int grade;
    private int workId;
    private Integer evaluatorId;

    public AssessmentCreateDTO(int grade, int workId, Integer evaluatorId) {
        this.grade = grade;
        this.workId = workId;
        this.evaluatorId = evaluatorId;
    }

    public AssessmentCreateDTO() {
    }

    public int getGrade() {
        return grade;
    }

    public int getWorkId() {
        return workId;
    }

    public Integer getEvaluatorId() {
        return evaluatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentCreateDTO)) return false;
        AssessmentCreateDTO that = (AssessmentCreateDTO) o;
        return grade == that.grade &&
                workId == that.workId &&
                Objects.equals(evaluatorId, that.evaluatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, workId, evaluatorId);
    }
}
