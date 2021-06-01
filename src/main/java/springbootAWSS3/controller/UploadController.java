package springbootAWSS3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springbootAWSS3.uploader.Uploader;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UploadController {
    private final Uploader uploader;

    @PostMapping("api/v1/upload")
    public String upload(@RequestParam("data")MultipartFile file) throws IOException {
        return uploader.upload(file, "testImage");
    }
}
