package cz.cvut.fit.baklaal1.server.business.repository;

import cz.cvut.fit.baklaal1.model.data.entity.Assessment;
import cz.cvut.fit.baklaal1.model.data.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
    public Set<Assessment> findAllByEvaluatorId(int evaluatorId);

    public Optional<Assessment> findByWork(Work work);
}
