package springbootAWSS3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springbootAWSS3.domain.Entity.FileInfo;
import springbootAWSS3.service.FileInfoService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileInfoController {

    private final FileInfoService fileInfoService;

    @PostMapping("api/v1/upload")
    public FileInfo upload(@RequestParam("data")MultipartFile file) throws IOException {
        return fileInfoService.upload(file);
    }

    @DeleteMapping("api/v1/upload/{id}")
    public Long delete(@PathVariable("id") Long id) {
       return fileInfoService.delete(id);
    }
}
