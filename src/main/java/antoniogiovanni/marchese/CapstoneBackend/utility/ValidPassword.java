package antoniogiovanni.marchese.CapstoneBackend.utility;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {

    String message() default "Password non valida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
