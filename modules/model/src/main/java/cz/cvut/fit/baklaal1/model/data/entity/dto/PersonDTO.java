package cz.cvut.fit.baklaal1.model.data.entity.dto;


import java.sql.Timestamp;
import java.util.Objects;

public abstract class PersonDTO<T_DTO extends PersonDTO<T_DTO>> extends BasicDTO<T_DTO> implements Comparable<PersonDTO<T_DTO>> {
    protected final String username;
    protected final String name;
    protected final Timestamp birthdate;

    public PersonDTO(int id, String username, String name, Timestamp birthdate) {
        super(id);
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
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

    @Override
    public int compareTo(PersonDTO<T_DTO> o) {
        if(this.equals(o)) return 0;
        int res = this.name.compareTo(o.name);
        if(res == 0) return Integer.compare(this.id, o.id);
        return res;
    }
}
