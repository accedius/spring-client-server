package cz.cvut.fit.baklaal1.data.entity.dto;

import java.sql.Timestamp;

public abstract class PersonCreateDTO {
    protected String username;
    protected String name;
    protected Timestamp birthDate;

    public PersonCreateDTO(String username, String name, Timestamp birthDate) {
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
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
