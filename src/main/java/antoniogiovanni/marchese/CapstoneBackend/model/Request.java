package antoniogiovanni.marchese.CapstoneBackend.model;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String questionUrl;
    private String solutionUrl;
    private Student student;
    @OneToMany(mappedBy = "request")
    private List<Solution> teacherList;

    @Enumerated(EnumType.STRING)
    private RequestState requestState;
    @OneToOne
    private Invoice invoice;
}
