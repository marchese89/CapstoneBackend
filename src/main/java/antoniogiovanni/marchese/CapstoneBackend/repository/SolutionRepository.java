package antoniogiovanni.marchese.CapstoneBackend.repository;

import antoniogiovanni.marchese.CapstoneBackend.model.Solution;
import antoniogiovanni.marchese.CapstoneBackend.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Long> {

    @Query("SELECT s FROM Solution s WHERE s.request.id=:requestId AND s.request.requestState = 'OPEN'")
    List<Solution> getSolutionsByRequestId(@Param("requestId") Long requestId);
}
