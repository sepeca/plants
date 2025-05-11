package sia.plants.service.imageHandler;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class ImageHandlerImpl implements ImageHandler{
    @Override
    public List<String> imagesToUrls(List<MultipartFile> images){
        List<String> imageUrls = new ArrayList<>();
        Path uploadDir = Paths.get("uploads");

        for (MultipartFile image : images) {
            imageUrls.add(converter(image, uploadDir));
        }

        return imageUrls;
    }
    @Override
    public String imageToUrl(MultipartFile image){
        Path uploadDir = Paths.get("uploads");
        return converter(image, uploadDir);
    }
    @Override
    public void deleteFileFromDisk(String url) {


            String fileName = Paths.get(URI.create(url).getPath()).getFileName().toString();
            System.out.println("ИМЯ ФАЙЛА: " + fileName);
            Path path = Paths.get("uploads", fileName);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + path + " — " + e.getMessage());
            }

    }

    private String converter(MultipartFile image,Path uploadDir){
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        try {
            Files.write(filePath, image.getBytes());
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + fileName, e);
        }
    }
}
