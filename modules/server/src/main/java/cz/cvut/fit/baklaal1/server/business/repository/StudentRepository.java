package cz.cvut.fit.baklaal1.server.business.repository;

import cz.cvut.fit.baklaal1.model.data.entity.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends PersonRepository<Student> {
}
