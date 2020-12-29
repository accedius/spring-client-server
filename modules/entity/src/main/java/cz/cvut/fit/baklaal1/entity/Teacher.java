package cz.cvut.fit.baklaal1.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import cz.cvut.fit.baklaal1.model.data.helper.DBConstants;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Entity
public class Teacher extends Person implements ConvertibleToDTO<TeacherDTO>, ConvertibleToCreateDTO<TeacherCreateDTO> {
    @NotNull
    private double wage;

    @OneToMany(mappedBy = DBConstants.EVALUATOR)
    @SortNatural
    @OrderBy
    private Set<Assessment> assessments = new TreeSet<>();

    public Teacher() {}

    public Teacher(String username, String name, Timestamp birthdate, double wage) {
        super(username, name, birthdate);
        this.wage = wage;
    }

    public Teacher(String username, String name, Timestamp birthdate, double wage, Set<Assessment> assessments) {
        super(username, name, birthdate);
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
        int id = this.getId() == null ? -1 : this.getId();
        String username = this.getUsername();
        String name = this.getName();
        Timestamp birthdate = this.getBirthdate();

        double wage = this.getWage();
        Set<Assessment> assessments = this.getAssessments();
        Set<Integer> assessmentIds = assessments.stream().map(Assessment::getId).collect(Collectors.toCollection(TreeSet::new));

        return new TeacherDTO(id, username, name, birthdate, wage, assessmentIds);
    }

    @Override
    public TeacherCreateDTO toCreateDTO() {
        String username = this.getUsername();
        String name = this.getName();
        Timestamp birthdate = this.getBirthdate();

        double wage = this.getWage();
        Set<Assessment> assessments = this.getAssessments();
        Set<Integer> assessmentIds = assessments.stream().map(Assessment::getId).collect(Collectors.toCollection(TreeSet::new));

        return new TeacherCreateDTO(username, name, birthdate, wage, assessmentIds);
    }
}
