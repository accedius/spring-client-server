package cz.cvut.fit.baklaal1.model.data.entity.dto;

import cz.cvut.fit.baklaal1.model.data.entity.Person;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.Objects;

public abstract class PersonDTO<T_DTO extends PersonDTO<T_DTO>> extends RepresentationModel<T_DTO> implements ReadableId {
    protected int id;
    protected String username;
    protected String name;
    protected Timestamp birthdate;

    public PersonDTO(int id, String username, String name, Timestamp birthdate) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
    }

    public PersonDTO(final Person person) {
        this.id = person.getId() == null ? -1 : person.getId();
        this.username = person.getUsername();
        this.name = person.getName();
        this.birthdate = person.getBirthdate();
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

    public Timestamp getBirthdate() {
        return birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().equals(this.getClass()))) return false;
        PersonDTO personDTO = (PersonDTO) o;
        if(id != -1 && personDTO.getId() != -1 && id != personDTO.getId()) return false;
        return id == personDTO.getId() &&
                username.equals(personDTO.getUsername()) &&
                name.equals(personDTO.getName()) &&
                Objects.equals(birthdate, personDTO.getBirthdate());
    }
}
