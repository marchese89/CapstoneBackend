package antoniogiovanni.marchese.CapstoneBackend.repository;

import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    @Query("SELECT r FROM Request r WHERE r.subject IN (SELECT t.subjectList FROM Teacher t WHERE t.id=:teacherId)")
    Page<Request> getRequestByTeacher(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.subject IN (SELECT t.subjectList FROM Teacher t WHERE t.id=:teacherId)")
    List<Request> getRequestByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT r FROM Request r WHERE r IN (SELECT s.requestList From Student s WHERE s.id=:studentId)")
    List<Request> getRequestByStudent(@Param("studentId") Long studentId);
    @Query("SELECT r FROM Request r WHERE r IN (SELECT s.requestList From Student s WHERE s.id=:studentId)")
    Page<Request> getRequestByStudent(@Param("studentId") Long studentId,Pageable pageable);
}
