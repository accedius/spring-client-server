package cz.cvut.fit.baklaal1.server.business.repository;

import cz.cvut.fit.baklaal1.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Integer> {
    Optional<T> findByUsername(String username);

    Set<T> findAllByName(String name);
}
