package cz.cvut.fit.baklaal1.business.repository;

import cz.cvut.fit.baklaal1.data.entity.Assessment;
import cz.cvut.fit.baklaal1.data.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
    public List<Assessment> findAllByEvaluatorId(int evaluatorId);

    public Optional<Assessment> findByWork(Work work);
}
