package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Assessment;

public class AssessmentDTO {
    private final int id;
    private final int grade;
    private final int workId;
    private final Integer evaluatorId;

    public AssessmentDTO(int id, int grade, int workId, Integer evaluatorId) {
        this.id = id;
        this.grade = grade;
        this.workId = workId;
        this.evaluatorId = evaluatorId;
    }

    public AssessmentDTO(Assessment assessment) {
        this.id = assessment.getId();
        this.grade = assessment.getGrade();
        this.workId = assessment.getWork().getId();
        this.evaluatorId = assessment.getEvaluator() != null ? assessment.getEvaluator().getId() : null;
    }

    public int getId() {
        return id;
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
