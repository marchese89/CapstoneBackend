package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.utility.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginDTO(
        @Email(message = "email not valid")
        @NotBlank
        String email,
        @NotBlank
        @ValidPassword(message = "password not valid")
        String password) {
}
