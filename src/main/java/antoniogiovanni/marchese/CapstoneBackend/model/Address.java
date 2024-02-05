package antoniogiovanni.marchese.CapstoneBackend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String street;
    private String houseNumber;

    private String city;
    private String province;

    private String postalCode;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
