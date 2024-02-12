package antoniogiovanni.marchese.CapstoneBackend.payloads;

import jakarta.validation.constraints.NotNull;

public record FeedbackDTO (
        @NotNull Long requestId,
        @NotNull
        Integer score){
}
