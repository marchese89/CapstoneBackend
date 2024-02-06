package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RequestDTO(
        @NotEmpty(message = "title cannot be empty")
        @NotNull(message = "title cannot be null")
        String title,
        @NotNull(message = "subjectId cannot be null")
        Long subjectId
) {
}
