package cz.cvut.fit.baklaal1.data.entity.dto;

public class AssessmentCreateDTO {
    private final int grade;
    private final int workId;
    private final Integer evaluatorId;

    public AssessmentCreateDTO(int grade, int workId, Integer evaluatorId) {
        this.grade = grade;
        this.workId = workId;
        this.evaluatorId = evaluatorId;
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
}
