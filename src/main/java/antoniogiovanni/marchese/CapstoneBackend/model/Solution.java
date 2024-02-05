package antoniogiovanni.marchese.CapstoneBackend.model;

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
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private String solutionUrl;
}
