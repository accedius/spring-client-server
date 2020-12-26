package cz.cvut.fit.baklaal1.entity;

import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.StudentDTO;
import cz.cvut.fit.baklaal1.model.data.helper.DBConstants;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Entity
public class Student extends Person implements ConvertibleToDTO<StudentDTO>, ConvertibleToCreateDTO<StudentCreateDTO> {
    //I know, it's a bad practice to store db-derived values in non buffer entities, but I just want to try it. Exists only as a buffer, updated only on work insertion -> possible inconsistency source
    private float averageGrade;

    @ManyToMany(mappedBy = DBConstants.AUTHORS)
    @SortNatural
    @OrderBy("title ASC")
    private Set<Work> works = new TreeSet<>();

    public Student() {}

    public Student(String username, String name, Timestamp birthDate, float averageGrade) {
        super(username, name, birthDate);
        this.averageGrade = averageGrade;
    }

    public Student(String username, String name, Timestamp birthDate, float averageGrade, Set<Work> works) {
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

    public Set<Work> getWorks() {
        return works;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    @Override
    public StudentDTO toDTO() {
        int id = this.getId() == null ? -1 : this.getId();
        String username = this.getUsername();
        String name = this.getName();
        Timestamp birthdate = this.getBirthdate();

        float averageGrade = this.getAverageGrade();
        Set<Work> works = this.getWorks();
        Set<Integer> workIds = works.stream().map(Work::getId).collect(Collectors.toSet());

        return new StudentDTO(id, username, name, birthdate, averageGrade, workIds);
    }

    @Override
    public StudentCreateDTO toCreateDTO() {
        String username = this.getUsername();
        String name = this.getName();
        Timestamp birthdate = this.getBirthdate();

        float averageGrade = this.getAverageGrade();
        Set<Work> works = this.getWorks();
        Set<Integer> workIds = works.stream().map(Work::getId).collect(Collectors.toSet());

        return new StudentCreateDTO(username, name, birthdate, averageGrade, workIds);
    }
}
