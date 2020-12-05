package cz.cvut.fit.baklaal1.data.entity;

import cz.cvut.fit.baklaal1.data.helper.DBConstants;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends Person {
    //I know, it's a bad practice to store db-derived values in non buffer entities, but I just want to try it. Exists only as a buffer, updated only on work insertion -> possible inconsistency source
    private float averageGrade;

    @ManyToMany(mappedBy = DBConstants.AUTHORS)
    private List<Work> works = new ArrayList<>();

    public Student() {}

    public Student(String username, String name, Timestamp birthDate, float averageGrade) {
        super(username, name, birthDate);
        this.averageGrade = averageGrade;
    }

    public Student(String username, String name, Timestamp birthDate, float averageGrade, List<Work> works) {
        super(username, name, birthDate);
        this.averageGrade = averageGrade;
        this.works = works;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(float averageGrade) {
        this.averageGrade = averageGrade;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }
}
