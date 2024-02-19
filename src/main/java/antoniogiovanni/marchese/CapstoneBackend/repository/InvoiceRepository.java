package antoniogiovanni.marchese.CapstoneBackend.repository;

import antoniogiovanni.marchese.CapstoneBackend.model.Invoice;
import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("SELECT COALESCE(MAX(i.number), 0) FROM Invoice i  ")
    long getMaxInvoiceNumber();
    @Query("SELECT DISTINCT YEAR(i.issuingDate) FROM Invoice i WHERE :teacherId IN (SELECT s.teacher.id FROM Solution s WHERE s.request = i.request AND s.state = 'ACCEPTED')")
    List<Integer> findDistinctYears(@Param("teacherId") Long teacherId);

    @Query("SELECT i FROM Invoice i WHERE :teacherId IN (SELECT s.teacher.id FROM Solution s WHERE s.request = i.request AND s.state = 'ACCEPTED') AND YEAR(i.issuingDate) = :year AND MONTH(i.issuingDate) = :month")
    Page<Invoice> getInvoicesByTeacher(@Param("teacherId") Long teacherId,@Param("year") Integer year, @Param("month") Integer month, Pageable pageable);
}
