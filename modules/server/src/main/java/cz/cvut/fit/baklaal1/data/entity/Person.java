package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Comparable<Person> {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    protected String username;

    @NotNull
    protected String name;

    protected Timestamp birthdate;

    public Person() {}

    public Person(String username, String name, Timestamp birthdate) {
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Timestamp birthDate) {
        this.birthdate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        if(id != null && person.id != null && !id.equals(person.id)) return false;
        return username.equals(person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.name);
    }
}
