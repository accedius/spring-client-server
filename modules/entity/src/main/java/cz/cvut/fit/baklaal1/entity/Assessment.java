package cz.cvut.fit.baklaal1.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.AssessmentDTO;
import cz.cvut.fit.baklaal1.model.data.helper.DBConstants;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Assessment implements Comparable<Assessment>, ConvertibleToDTO<AssessmentDTO>, ConvertibleToCreateDTO<AssessmentCreateDTO> {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private int grade;

    @NotNull
    @OneToOne(mappedBy = DBConstants.ASSESSMENT)
    private Work work;

    @ManyToOne
    @JoinColumn(name = DBConstants.EVALUATOR_ID, nullable = false)
    private Teacher evaluator;

    public Assessment() {}

    public Assessment(int grade) {
        this.grade = grade;
    }

    public Assessment(int grade, Work work, Teacher evaluator) {
        this.grade = grade;
        this.work = work;
        this.evaluator = evaluator;
    }

    public Integer getId() {
        return id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Teacher getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Teacher evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assessment)) return false;
        Assessment assessment = (Assessment) o;
        if(id != null && assessment.id != null && !id.equals(assessment.id)) return false;
        return work.equals(assessment.work);
    }

    @Override
    public int hashCode() {
        return Objects.hash(work);
    }

    @Override
    public int compareTo(Assessment o) {
        if(this.equals(o)) return 0;
        if(id == null || o.id == null) return Integer.compare(this.work.getId(), o.work.getId());
        return id.compareTo(o.id);
    }

    @Override
    public AssessmentDTO toDTO() {
        int id = this.getId() == null ? -1 : this.getId();
        int grade = this.getGrade();
        int workId = this.getWork().getId();
        Integer evaluatorId = this.getEvaluator() != null ? this.getEvaluator().getId() : null;
        return new AssessmentDTO(id, grade, workId, evaluatorId);
    }

    @Override
    public AssessmentCreateDTO toCreateDTO() {
        int grade = this.getGrade();
        int workId = this.getWork().getId();
        Integer evaluatorId = this.getEvaluator() != null ? this.getEvaluator().getId() : null;
        return new AssessmentCreateDTO(grade, workId, evaluatorId);
    }
}
