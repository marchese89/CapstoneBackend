package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.Address;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.AddressModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.StudentModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.service.AddressService;
import antoniogiovanni.marchese.CapstoneBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER','ADMIN')")
    public Address modifyAddress(@PathVariable Long id,
                                 @RequestBody @Validated AddressModifyDTO addressModifyDTO,
                                 BindingResult validation,
                                 @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        return addressService.findByIdAndUpdate(id,addressModifyDTO,currentUser);
    }
}
