package antoniogiovanni.marchese.CapstoneBackend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private long number;
    private String invoiceFileUrl;

    private LocalDate issuingDate;
    @OneToOne(mappedBy = "invoice")
    private Request request;
}
