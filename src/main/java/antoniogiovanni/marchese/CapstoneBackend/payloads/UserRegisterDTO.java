package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.utility.ValidPassword;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public record UserRegisterDTO(
        @NotNull
        @NotEmpty
        @NotBlank
        String name,
        @NotNull
        @NotEmpty
        @NotBlank
        String surname,
        @Email
        @NotBlank
        String email,
        @ValidPassword
        @NotBlank
        String password,
        @NotNull
        @NotEmpty
        @NotBlank
        @Size(min = 16,max = 16)
        String cf,
        @NotNull
        @Enumerated(EnumType.STRING)
        @NotNull
        Role role,
        @NotNull
        @NotEmpty
        @NotBlank
        String street,
        @NotNull
        @NotEmpty
        @NotBlank
        String houseNumber,
        @NotNull
        @NotEmpty
        @NotBlank
        String city,
        @NotNull
        @NotEmpty
        @NotBlank
        @Size(min = 2,max = 2)
        String province,
        @NotNull
        @NotEmpty
        @NotBlank
        @Size(min = 5,max = 5)
        String postalCode,
        String piva
        ) {
}
