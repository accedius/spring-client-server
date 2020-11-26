package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.data.helper.DBConstants;

import javax.persistence.*;

@Entity
public class Assessment {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private int grade;

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

    public int getId() {
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
}
