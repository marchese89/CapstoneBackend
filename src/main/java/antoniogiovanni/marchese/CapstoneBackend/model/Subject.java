package antoniogiovanni.marchese.CapstoneBackend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "subjectList")
    private List<Teacher> teacherList;
}
