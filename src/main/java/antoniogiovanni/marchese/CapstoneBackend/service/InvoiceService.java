package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.model.Invoice;
import antoniogiovanni.marchese.CapstoneBackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;



//    public Invoice save()



    public Long getMaxInvoiceNumber(){
        return invoiceRepository.getMaxInvoiceNumber();
    }

    public Invoice save(Invoice invoice){
       return invoiceRepository.save(invoice);
    }
}
