package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.data.helper.DBConstants;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Assessment implements Comparable<Assessment> {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private int grade;

    @NotNull
    @OneToOne(mappedBy = DBConstants.ASSESSMENT)
    private Work work;

    @ManyToOne
    @JoinColumn(name = DBConstants.EVALUATOR_ID)
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
        return  grade == assessment.grade &&
                work.equals(assessment.work);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, work);
    }

    @Override
    public int compareTo(Assessment o) {
        return id.compareTo(o.id);
    }
}
