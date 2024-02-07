package antoniogiovanni.marchese.CapstoneBackend.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Element with id " + id + " not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
