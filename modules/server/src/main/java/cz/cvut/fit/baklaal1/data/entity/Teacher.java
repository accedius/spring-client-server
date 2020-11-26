package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.data.helper.DBConstants;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends Person {
    @NotNull
    private double wage;

    @OneToMany(mappedBy = DBConstants.EVALUATOR)
    private List<Assessment> assessments = new ArrayList<>();

    public Teacher() {}

    public Teacher(String username, String name, Timestamp birthDate, double wage) {
        super(username, name, birthDate);
        this.wage = wage;
    }

    public Teacher(String username, String name, Timestamp birthDate, double wage, List<Assessment> assessments) {
        super(username, name, birthDate);
        this.wage = wage;
        this.assessments = assessments;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }
}
