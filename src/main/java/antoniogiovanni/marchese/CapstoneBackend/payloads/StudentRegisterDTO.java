package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.utility.ValidPassword;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentRegisterDTO(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String surname,
        @Email
        String email,
        @ValidPassword
        String password,
        @NotNull
        @NotEmpty
        @Size(min = 16,max = 16)
        String cf,
        @NotNull
        @Enumerated(EnumType.STRING)
        Role role,
        @NotNull
        @NotEmpty
        String street,
        @NotNull
        @NotEmpty
        String houseNumber,
        @NotNull
        @NotEmpty
        String city,
        @NotNull
        @NotEmpty
        @Size(min = 2,max = 2)
        String province,
        @NotNull
        @NotEmpty
        @Size(min = 5,max = 5)
        String postalCode
        ) {
}
