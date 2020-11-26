package cz.cvut.fit.baklaal1.data.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    @GeneratedValue
    private int id;

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

    public Person(int id, String username, String name, Timestamp birthDate) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
    }

    public int getId() {
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
}
