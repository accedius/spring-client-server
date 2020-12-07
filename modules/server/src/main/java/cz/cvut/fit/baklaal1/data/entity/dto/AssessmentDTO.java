package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Assessment;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class AssessmentDTO extends RepresentationModel<AssessmentDTO> implements ReadableId {
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

    public AssessmentDTO(final Assessment assessment) {
        this.id = assessment.getId() == null ? -1 : assessment.getId();
        this.grade = assessment.getGrade();
        this.workId = assessment.getWork().getId();
        this.evaluatorId = assessment.getEvaluator() != null ? assessment.getEvaluator().getId() : null;
    }

    public int getId() {
        return id;
    }

    @Override
    public int readId() {
        return getId();
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
        if (!(o instanceof AssessmentDTO)) return false;
        AssessmentDTO assessmentDTO = (AssessmentDTO) o;
        if(id != -1 && assessmentDTO.id != -1 && id != assessmentDTO.id) return false;
        return grade == assessmentDTO.grade &&
                workId == assessmentDTO.workId &&
                Objects.equals(evaluatorId, assessmentDTO.evaluatorId);
    }
}
