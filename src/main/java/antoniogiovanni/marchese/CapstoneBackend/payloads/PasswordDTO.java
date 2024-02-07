package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.utility.ValidPassword;

public record PasswordDTO(
        @ValidPassword
        String password) {
}
