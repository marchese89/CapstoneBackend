package antoniogiovanni.marchese.CapstoneBackend.payloads;

public record PaymentConfirmDTO(String paymentMethodId, Long amount, String currency,Long solutionId) {
}
