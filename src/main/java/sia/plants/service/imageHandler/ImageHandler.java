package sia.plants.service.imageHandler;

import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface ImageHandler {
    List<String> imagesToUrls(List<MultipartFile> images);
    String imageToUrl(MultipartFile image);
    void deleteFileFromDisk(String url);
}
