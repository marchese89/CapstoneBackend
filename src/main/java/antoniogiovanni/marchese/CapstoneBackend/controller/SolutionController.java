package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.Solution;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.Teacher;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.ResponseDTO;
import antoniogiovanni.marchese.CapstoneBackend.service.SolutionService;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Value("${upload.dir}")
    private String uploadDir;
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{requestId}/{price}")
    public ResponseDTO addSolution(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long requestId,
            @PathVariable Long price,
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
        if(!solutionService.canSaveSolution(requestId,(Teacher) currentUser)){
            throw new UnauthorizedException("you cannot send more than one solution per request");
        }

        return new ResponseDTO(solutionService.save(file,requestId,price,(Teacher) currentUser).getId());
    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getByRequestId/{requestId}")
    public List<Solution> getSolutionsByRequestId(@PathVariable Long requestId,@AuthenticationPrincipal User currentUser){
        return solutionService.getSolutionsByRequestId(requestId,(Student)currentUser);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getByRequestIdAndTeacher/{requestId}")
    public Solution getSolutionsByRequestIdAndTeacher(@PathVariable Long requestId,@AuthenticationPrincipal User currentUser){
        return solutionService.getSolutionByRequestIdAndTeacher(requestId,(Teacher)currentUser);
    }

//    @PreAuthorize("hasAnyAuthority('STUDENT')")
//    @PutMapping("/acceptSolution/{solutionId}")
//    public ResponseDTO acceptSolution(@PathVariable Long solutionId,@AuthenticationPrincipal User currentUser) throws IOException, MessagingException {
//        return new ResponseDTO(solutionService.acceptSolution(solutionId,(Student) currentUser).getId());
//    }
}
