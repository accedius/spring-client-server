package cz.cvut.fit.baklaal1.server.business.repository;

import cz.cvut.fit.baklaal1.entity.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends PersonRepository<Student> {
}
