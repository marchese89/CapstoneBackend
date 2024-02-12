package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.FeedbackResponseDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.PasswordDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.ResponseDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.service.RequestService;
import antoniogiovanni.marchese.CapstoneBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @PutMapping
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    public User modifyUser(@RequestBody @Validated UserModifyDTO userModifyDTO, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        return userService.update(userModifyDTO, currentUser);
    }
    @PutMapping("/modPass")
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    public ResponseDTO modifyUserPassword(@RequestBody @Validated PasswordDTO passwordDTO, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        return new ResponseDTO(userService.updatePassword(passwordDTO, currentUser).getId());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    public User getUser(@PathVariable Long id){
        return userService.findById(id);
    }

}
