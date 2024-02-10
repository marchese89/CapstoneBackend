package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.payloads.FileRequestDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    public ResponseEntity<Resource> getFile(@RequestBody FileRequestDTO fileRequestDTO) throws MalformedURLException {
        //File file = new File(fileRequestDTO.path());
        Path path = Paths.get(fileRequestDTO.path());
        Resource resource = new UrlResource(path.toUri());

        //            byte[] fileContent = Files.readAllBytes(file.toPath());
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            ContentDisposition contentDisposition = ContentDisposition.inline().filename(file.getName()).build();
//            headers.setContentDisposition(contentDisposition);
//            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

