package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubjectDTO(
        @NotNull(message = "subject name cannot be null")
        @NotEmpty(message = "subject name cannot be empty")
        String name) {
}
