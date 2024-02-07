package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.ResponseDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
import antoniogiovanni.marchese.CapstoneBackend.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO createSubject(@RequestBody @Validated SubjectDTO subjectDTO,
                                     BindingResult validation,
                                     @AuthenticationPrincipal User currentUser){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        return  new ResponseDTO(subjectService.save(subjectDTO,currentUser).getId());
    }
    @PostMapping("/add/{subjectId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO addSubject(@PathVariable Long subjectId,
                                     @AuthenticationPrincipal User currentUser){
        return  new ResponseDTO(subjectService.addSubject(subjectId,currentUser).getId());
    }
}
