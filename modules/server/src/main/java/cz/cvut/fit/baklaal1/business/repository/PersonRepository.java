package cz.cvut.fit.baklaal1.business.repository;

import cz.cvut.fit.baklaal1.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository<T extends Person> extends JpaRepository<T, Integer> {
    Optional<T> findByUsername(String username);

    List<T> findAllByName(String name);
}
