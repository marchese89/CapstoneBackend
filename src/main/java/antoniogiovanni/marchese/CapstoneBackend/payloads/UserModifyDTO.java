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
                               String cf) {
}
