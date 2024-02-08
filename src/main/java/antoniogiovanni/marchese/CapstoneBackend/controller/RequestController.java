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
        String originalFileName = file.getOriginalFilename();

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String filePath;
        long n = 1;
        File f = new File(uploadDir + File.separator + "requests"+ File.separator + n + fileExtension);

        while(f.exists()){
            n++;
            f = new File(uploadDir + File.separator + "requests"+ File.separator + n + fileExtension);
        }
        try {
            byte[] bytes = file.getBytes();
            filePath = uploadDir + File.separator + "requests"+ File.separator + n + fileExtension;
            Files.write(Path.of(filePath), bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("problems during upload");
        }
        RequestDTO requestDTO = new RequestDTO(requestTitle,subjectId);
        return new ResponseDTO(requestService.save(filePath,(Student) currentUser,requestDTO).getId());
    }

    @GetMapping("/byTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<Request> getRequestsByTeacher(@AuthenticationPrincipal User currentUser) {
        return requestService.getRequestByTeacher(currentUser.getId());
    }
}
