package antoniogiovanni.marchese.CapstoneBackend.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}