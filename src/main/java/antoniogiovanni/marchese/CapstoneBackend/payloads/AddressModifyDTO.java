package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddressModifyDTO(
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
        String province,
        @NotNull(message = "postalCode cannot be null")
        @NotEmpty(message = "postalCode cannot be empty")
        String postalCode
) {
}
