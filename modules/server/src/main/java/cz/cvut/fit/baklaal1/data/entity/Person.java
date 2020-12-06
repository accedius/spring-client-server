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

    protected Timestamp birthDate;

    public Person() {}

    public Person(String username, String name, Timestamp birthDate) {
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
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

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id == person.id &&
                username.equals(person.username) &&
                name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name);
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.name);
    }
}
