package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.model.Invoice;
import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/yearsByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<Integer> findDistinctYears(@AuthenticationPrincipal User currentUser){
        return invoiceService.findDistinctYears(currentUser.getId());
    }
    @GetMapping("/byTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Page<Invoice> getInvoicesByTeacher(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String orderBy,
                                              @RequestParam(defaultValue = "desc") String direction,
                                              @AuthenticationPrincipal User currentUser,
                                              @RequestParam(required = false, defaultValue = "2024") Integer year,
                                              @RequestParam(required = false, defaultValue = "1") Integer month){
        return invoiceService.getInvoicesByTeacher(currentUser.getId(),year, month, page, size, orderBy,direction);
    }
    @GetMapping("/{invoiceId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Invoice getInvoiceById(@PathVariable Long invoiceId){
        return invoiceService.getById(invoiceId);
    }
}
