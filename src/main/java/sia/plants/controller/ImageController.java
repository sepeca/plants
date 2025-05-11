package sia.plants.controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
public class ImageController {
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(Files.probeContentType(imagePath) != null ?
                            MediaType.parseMediaType(Files.probeContentType(imagePath)) :
                            MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
