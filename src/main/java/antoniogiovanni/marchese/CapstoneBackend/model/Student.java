package antoniogiovanni.marchese.CapstoneBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student extends User{

    private String cf;
}
