package cz.cvut.fit.baklaal1.data.entity;

import cz.cvut.fit.baklaal1.data.helper.DBConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Work {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    private String text;

    //author(s) should exist in time of the creation => no works without authors can be created, but works will remain after author(s) deletion
    @ManyToMany
    @JoinTable(name = DBConstants.STUDENT_WORK,
            joinColumns = @JoinColumn(name = DBConstants.WORK_ID),
            inverseJoinColumns = @JoinColumn(name = DBConstants.STUDENT_ID)
    )
    private List<Student> authors = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = DBConstants.ASSESSMENT_ID, referencedColumnName = DBConstants.ID)
    private Assessment assessment;

    public Work() {}

    public Work(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Work(String title, String text, List<Student> authors, Assessment assessment) {
        this.title = title;
        this.text = text;
        this.authors = authors;
        this.assessment = assessment;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Student> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Student> authors) {
        this.authors = authors;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
