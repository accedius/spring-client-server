package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.data.helper.DBConstants;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Teacher extends Person implements ConvertibleToDTO<TeacherDTO> {
    @NotNull
    private double wage;

    @OneToMany(mappedBy = DBConstants.EVALUATOR)
    @SortNatural
    @OrderBy
    private Set<Assessment> assessments = new TreeSet<>();

    public Teacher() {}

    public Teacher(String username, String name, Timestamp birthDate, double wage) {
        super(username, name, birthDate);
        this.wage = wage;
    }

    public Teacher(String username, String name, Timestamp birthDate, double wage, Set<Assessment> assessments) {
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

    public Set<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<Assessment> assessments) {
        this.assessments = assessments;
    }

    @Override
    public TeacherDTO toDTO() {
        return new TeacherDTO(this);
    }
}
