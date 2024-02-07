package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotNull(message = "street cannot be null")
        @NotEmpty(message = "street cannot be empty")
        String street,
        @NotNull(message = "houseNumber cannot be null")
        @NotEmpty(message = "houseNumber cannot be empty")
        String houseNumber,
        @NotNull(message = "city cannot be null")
        @NotEmpty(message = "city cannot be empty")
        String city,
        @NotNull(message = "province cannot be null")
        @NotEmpty(message = "province cannot be empty")
        @Size(min = 2,max = 2)
        String province,
        @NotNull(message = "postal code cannot be null")
        @NotEmpty(message = "postal code cannot be empty")
        @Size(min = 5,max = 5)
        String postalCode
) {
}
