package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserModifyDTO(@NotNull
                            @NotEmpty
                            String name,
                            @NotNull
                            @NotEmpty
                            String surname,
                            @Email
                            String email,
                            @NotNull
                            @NotEmpty
                            @Size(min = 16,max = 16)
                            String cf,
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
                            String postalCode,
                            String piva
    ) {
}
