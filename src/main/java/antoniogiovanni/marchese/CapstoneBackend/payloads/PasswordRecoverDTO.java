package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.Email;

public record PasswordRecoverDTO(
        @Email
        String email) {
}
