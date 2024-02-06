package antoniogiovanni.marchese.CapstoneBackend.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Elemento con id " + id + " non trovato!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
