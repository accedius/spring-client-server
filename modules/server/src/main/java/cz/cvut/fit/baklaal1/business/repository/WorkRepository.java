package cz.cvut.fit.baklaal1.business.repository;

import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    List<Work> findAllByTitle(String title);

    List<Work> findAllByTitleAndAuthorsIn(String title, Set<Student> authors);
}
