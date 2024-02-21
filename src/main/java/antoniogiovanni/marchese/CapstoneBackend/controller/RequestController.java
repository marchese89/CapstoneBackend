package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.RequestDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.ResponseDTO;
import antoniogiovanni.marchese.CapstoneBackend.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/{subjectId}/{requestTitle}")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO createRequest(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long subjectId,
            @PathVariable String requestTitle,
            @AuthenticationPrincipal User currentUser
    ) {

        if (file.isEmpty()) {
            throw new BadRequestException("file cannot be empty");
        }
        //type validation
        String contentType = file.getContentType();
        if(!(contentType.startsWith("image/")||contentType.equalsIgnoreCase("application/pdf"))){
            throw new BadRequestException("unsupported file type");
        }


        RequestDTO requestDTO = new RequestDTO(requestTitle,subjectId);
        return new ResponseDTO(requestService.save(file,(Student) currentUser,requestDTO).getId());
    }

    @GetMapping("/byTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Page<Request> getRequestsByTeacher(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String direction,
            @AuthenticationPrincipal User currentUser) {
        return requestService.getRequestByTeacher(currentUser.getId(),page,size,orderBy,direction);
    }
    @GetMapping("/byStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public Page<Request> getRequestsByStudent(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String orderBy,
                                              @RequestParam(defaultValue = "desc") String direction,
                                              @AuthenticationPrincipal User currentUser) {
        return requestService.getRequestByStudent(currentUser.getId(),page,size,orderBy,direction);
    }
    @GetMapping("/student/{requestId}")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public Request getRequestByIdForStudents(@PathVariable Long requestId,@AuthenticationPrincipal User currentUser) {
        return requestService.getByIdForStudents(requestId,currentUser);
    }
    @GetMapping("/teacher/{requestId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Request getRequestByIdForTeachers(@PathVariable Long requestId,@AuthenticationPrincipal User currentUser) {
        return requestService.getByIdForTeachers(requestId,currentUser);
    }
}
