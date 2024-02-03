package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserLoginDTO;
import antoniogiovanni.marchese.CapstoneBackend.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO userLoginDTO) {
        User user = userService.findByEmail(userLoginDTO.email());
        if (bcrypt.matches(userLoginDTO.password(),user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Invalid credentials!");
        }
    }
}
