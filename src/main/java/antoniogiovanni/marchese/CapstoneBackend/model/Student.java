package antoniogiovanni.marchese.CapstoneBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Student extends User{

    private String cf;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Request> requestList;
}
