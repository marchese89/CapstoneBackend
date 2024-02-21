package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotBlank
        String street,
        @NotBlank
        String houseNumber,
        @NotBlank
        String city,
        @NotBlank
        @Size(min = 2,max = 2)
        String province,
        @Size(min = 5,max = 5)
        @NotBlank
        String postalCode
) {
}
