package cz.cvut.fit.baklaal1.server.business.repository;

import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    Set<Work> findAllByTitle(String title);

    Set<Work> findAllByTitleAndAuthorsIn(String title, Set<Student> authors);
}
