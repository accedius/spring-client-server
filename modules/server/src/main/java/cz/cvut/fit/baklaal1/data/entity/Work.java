package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.data.entity.dto.WorkDTO;
import cz.cvut.fit.baklaal1.data.helper.DBConstants;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Work implements Comparable<Work>, ConvertibleToDTO<WorkDTO>, ConvertibleToCreateDTO<WorkCreateDTO> {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String title;

    private String text;

    //author(s) should exist in time of the creation => no works without authors can be created, but works will remain after author(s) deletion
    @ManyToMany
    @JoinTable(name = DBConstants.STUDENT_WORK,
            joinColumns = @JoinColumn(name = DBConstants.WORK_ID),
            inverseJoinColumns = @JoinColumn(name = DBConstants.STUDENT_ID)
    )
    @SortNatural
    @OrderBy("name ASC")
    private Set<Student> authors = new TreeSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = DBConstants.ASSESSMENT_ID, referencedColumnName = DBConstants.ID)
    private Assessment assessment;

    public Work() {}

    public Work(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Work(String title, String text, Set<Student> authors, Assessment assessment) {
        this.title = title;
        this.text = text;
        this.authors = authors;
        this.assessment = assessment;
    }

    public Integer getId() {
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

    public Set<Student> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Student> authors) {
        this.authors = authors;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Work)) return false;
        Work work = (Work) o;
        if(id != null && work.id != null && !id.equals(work.id)) return false;
        return  title.equals(work.title) &&
                Objects.equals(text, work.text) &&
                authors.equals(work.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authors);
    }

    @Override
    public int compareTo(Work o) {
        return title.compareTo(o.title);
    }

    @Override
    public WorkDTO toDTO() {
        return new WorkDTO(this);
    }

    @Override
    public WorkCreateDTO toCreateDTO() {
        return new WorkCreateDTO(this);
    }
}
