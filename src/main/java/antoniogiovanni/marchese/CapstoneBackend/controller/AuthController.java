package antoniogiovanni.marchese.CapstoneBackend.controller;


import antoniogiovanni.marchese.CapstoneBackend.payloads.*;
import antoniogiovanni.marchese.CapstoneBackend.service.UserService;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        AuthenticateUserDTO authenticateUserDTO = authService.authenticateUser(body);
        return new UserLoginResponseDTO(authenticateUserDTO.accessToken(),authenticateUserDTO.user().getRole());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO createUser(@RequestBody @Validated UserRegisterDTO userRegisterDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        User newUser = userService.save(userRegisterDTO);
        return new ResponseDTO(newUser.getId());
    }

    @PostMapping("/recoverPass")
    @ResponseStatus(HttpStatus.OK)
    public void passwordRecover(@RequestBody @Validated PasswordRecoverDTO passwordRecoverDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        userService.recoverPassword(passwordRecoverDTO.email());
    }
}
