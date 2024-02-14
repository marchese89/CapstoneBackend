package antoniogiovanni.marchese.CapstoneBackend.exceptions;

import antoniogiovanni.marchese.CapstoneBackend.payloads.ErrorDTO;
import com.stripe.exception.StripeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Dovrà rispondere con un 400
    public ErrorDTO handleBadRequest(BadRequestException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Dovrà rispondere con un 404
    public ErrorDTO handleNotFound(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorDTO handleAccessDenied(AccessDeniedException ex) {
        return new ErrorDTO("Your role not allow you this action", LocalDateTime.now());
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(UnauthorizedException ex){
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(StripeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleStripeException(StripeException ex){
        return new ErrorDTO("stripe error",LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleDataIntegrityViolation(DataIntegrityViolationException ex){
        return new ErrorDTO("data constraint violation",LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Dovrà rispondere con un 500
    public ErrorDTO handleGenericError(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("server's problems", LocalDateTime.now());
    }

}
