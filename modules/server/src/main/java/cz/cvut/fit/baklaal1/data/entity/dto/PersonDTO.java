package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Person;

import java.sql.Timestamp;
import java.util.Objects;

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
        this.id = person.getId() == null ? -1 : person.getId();
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

    protected boolean equals(Person personDTO) {
        if(id != -1 && personDTO.getId() != -1 && id != personDTO.getId()) return false;
        return id == personDTO.getId() &&
                username.equals(personDTO.getUsername()) &&
                name.equals(personDTO.getName()) &&
                Objects.equals(birthDate, personDTO.getBirthDate());
    }
}
