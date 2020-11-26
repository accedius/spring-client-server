package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Person;

import java.sql.Timestamp;

public abstract class PersonDTO {
    protected int id;
    protected String username;
    protected String name;
    protected Timestamp birthDate;

    public PersonDTO(int id, String username, String name, Timestamp birthDate) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.name = person.getName();
        this.birthDate = person.getBirthDate();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }
}
