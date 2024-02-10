package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
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

import java.util.List;

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
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO addSubject(@PathVariable Long subjectId,
                                     @AuthenticationPrincipal User currentUser){
        return  new ResponseDTO(subjectService.addSubject(subjectId,currentUser).getId());
    }
    @GetMapping("/byTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> getSubjectsByTeacher(
                                  @AuthenticationPrincipal User currentUser){
        return  subjectService.getSubjectsByTeacher(currentUser.getId());
    }

    @GetMapping("/byNoTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> getSubjectsByNoTeacher(
            @AuthenticationPrincipal User currentUser){
        return  subjectService.getSubjectsByNoTeacher(currentUser.getId());
    }


    @PutMapping("/{subjectId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO modifySubject(
            @PathVariable Long subjectId,
            @RequestBody @Validated SubjectDTO subjectDTO,
            BindingResult validation,
            @AuthenticationPrincipal User currentUser){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
        return new ResponseDTO(subjectService.update(subjectId,subjectDTO,currentUser).getId());
    }

//    @DeleteMapping("/{subjectId}")
//    @PreAuthorize("hasAnyAuthority('TEACHER')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteSubject(@PathVariable Long subjectId,
//                              @AuthenticationPrincipal User currentUser){
//        subjectService.findByIdAndDelete(subjectId,currentUser);
//    }
        @GetMapping
        @PreAuthorize("hasAnyAuthority('STUDENT')")
        @ResponseStatus(HttpStatus.OK)
        public List<Subject> getAllSubjects(){
            return subjectService.findAll();
        }

}
