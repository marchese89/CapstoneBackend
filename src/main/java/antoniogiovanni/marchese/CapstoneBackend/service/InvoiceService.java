package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.model.Invoice;
import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import antoniogiovanni.marchese.CapstoneBackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;


    public Long getMaxInvoiceNumber(){
        return invoiceRepository.getMaxInvoiceNumber();
    }

    public Invoice save(Invoice invoice){
       return invoiceRepository.save(invoice);
    }

    public List<Integer> findDistinctYears(Long teacherId){
        return invoiceRepository.findDistinctYears(teacherId);
    }

    public Page<Invoice> getInvoicesByTeacher(Long teacherId, Integer year, Integer month, int page, int size, String orderBy,String direction){
        Sort.Direction sortDirection = Sort.Direction.DESC; // Default: decrescente

        if (direction != null && direction.equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(sortDirection, orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return invoiceRepository.getInvoicesByTeacher(teacherId,year,month,pageable);
    }

    public Invoice getById(Long invoiceId){
        return invoiceRepository.findById(invoiceId).orElseThrow(()->new NotFoundException(invoiceId));
    }
}
