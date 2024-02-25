package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubjectDTO(
        @NotBlank
        String name) {
}
