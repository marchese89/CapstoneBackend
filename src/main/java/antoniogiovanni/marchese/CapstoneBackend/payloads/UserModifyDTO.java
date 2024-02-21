package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.*;

public record UserModifyDTO(@NotBlank
                            String name,
                            @NotBlank
                            String surname,
                            @Email
                            @NotBlank
                            String email,
                            @NotBlank
                            @Size(min = 16,max = 16)
                            String cf,
                            @NotBlank
                            String street,
                            @NotBlank
                            String houseNumber,
                            @NotBlank
                            String city,
                            @NotBlank
                            @Size(min = 2,max = 2)
                            String province,
                            @NotBlank
                            @Size(min = 5,max = 5)
                            String postalCode,
                            String piva
    ) {
}
