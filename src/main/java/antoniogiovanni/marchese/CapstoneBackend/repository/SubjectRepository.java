package antoniogiovanni.marchese.CapstoneBackend.repository;

import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    @Query("SELECT s FROM Subject s WHERE :idTeacher IN (SELECT t.id FROM s.teacherList t) ORDER BY s.name")
    List<Subject> findByTeacher(Long idTeacher);
    @Query("SELECT s FROM Subject s WHERE :idTeacher NOT IN (SELECT t.id FROM s.teacherList t) ORDER BY s.name")
    List<Subject> findByNoTeacher(Long idTeacher);
}
