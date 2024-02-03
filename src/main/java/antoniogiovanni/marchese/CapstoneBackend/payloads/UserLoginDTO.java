package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.utility.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginDTO(
        @Email(message = "email not valid")
        @NotEmpty(message = "email cannot be empty")
        @NotNull(message = "email cannot be null")
        String email,
        @ValidPassword(message = "password not valid")
        String password) {
}
