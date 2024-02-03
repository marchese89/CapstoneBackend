package antoniogiovanni.marchese.CapstoneBackend.utility;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.isEmpty()) {
            return false;
        }


        if (password.length() < 10) {
            return false;
        }

        //at least one of these symbols
        String regex1 = ".*[.:;?!@#,><\\[\\]{}].*";

        Pattern pattern = Pattern.compile(regex1);

        Matcher matcher1 = pattern.matcher(password);
        //no more than 2 equal symbols
        String regex2 = "^(?!.*(.)\\1\\1).*$";

        Pattern pattern2 = Pattern.compile(regex2);

        Matcher matcher2 = pattern2.matcher(password);

        return matcher1.matches() && matcher2.matches();
    }
}
