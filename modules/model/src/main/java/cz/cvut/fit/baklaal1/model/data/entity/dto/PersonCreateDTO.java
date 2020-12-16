package cz.cvut.fit.baklaal1.model.data.entity.dto;

import java.sql.Timestamp;
import java.util.Objects;

public abstract class PersonCreateDTO {
    protected static final int defaultBirthdateTime = 0;

    protected String username;
    protected String name;
    protected Timestamp birthdate;

    public PersonCreateDTO(String username, String name, Timestamp birthdate) {
        this.username = username;
        this.name = name;
        setBirthdate(birthdate);
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
        if (!(o instanceof PersonCreateDTO)) return false;
        PersonCreateDTO that = (PersonCreateDTO) o;
        return username.equals(that.username) &&
                name.equals(that.name) &&
                Objects.equals(birthdate, that.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, birthdate);
    }

    protected void setBirthdate(Timestamp birthdate) {
        if(birthdate != null) {
            this.birthdate = birthdate;
        } else {
            birthdate = new Timestamp(defaultBirthdateTime);
        }
    }
}
