package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;

import java.util.Objects;

public class AssessmentDTO extends BasicDTO<AssessmentDTO> implements Comparable<AssessmentDTO> {
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
    public void print() {
        System.out.println("Assessment: {");
        printFormatted("id", id);
        printFormatted("grade", grade);
        printFormatted("workId", workId);
        printFormatted("evaluatorId (teacherId)", evaluatorId);
        System.out.println(super.toString());
        System.out.println("}");
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

    @Override
    public int compareTo(AssessmentDTO o) {
        if(this.equals(o)) return 0;
        if(this.id == -1 || o.id == -1) return Integer.compare(this.workId, o.workId);
        return Integer.compare(this.id, o.id);
    }
}
