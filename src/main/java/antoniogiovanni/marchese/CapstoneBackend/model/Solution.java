package antoniogiovanni.marchese.CapstoneBackend.model;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Entity
@Table(name = "solutions")
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private String solutionUrl;
    @Enumerated(EnumType.STRING)
    private SolutionState state;
    private Long price;
}
