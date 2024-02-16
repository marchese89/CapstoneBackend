package antoniogiovanni.marchese.CapstoneBackend.model;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String questionUrl;
    private String solutionUrl;
    private String title;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToMany(mappedBy = "request")
    @JsonIgnore
    private List<Solution> solutionList;

    @Enumerated(EnumType.STRING)
    private RequestState requestState;
    @OneToOne(mappedBy = "request")
    private Invoice invoice;
    @OneToOne(mappedBy = "request")
    private Feedback feedback;
    private LocalDateTime date;
    private String paymentId;
}
