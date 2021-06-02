package springbootAWSS3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springbootAWSS3.common.uploader.Uploader;
import springbootAWSS3.domain.Entity.FileInfo;
import springbootAWSS3.repository.FileInfoRepository;
import springbootAWSS3.service.FileInfoService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileInfoController {

    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;
    private final Uploader uploader;

    @PostMapping("api/v1/upload")
    public FileInfo upload(@RequestParam("data")MultipartFile file) throws IOException {
        return fileInfoService.upload(file);
    }

    @DeleteMapping("api/v1/upload/{id}")
    public Long delete(@PathVariable("id") Long id) {
       return fileInfoService.delete(id);
    }

    @GetMapping("api/v1/download/{id}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("id") Long id,
                                                 HttpServletRequest request) throws IOException {
        FileInfo fileInfo = fileInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재 하지 않는 파일"));

        String filename = getFileNameByBrowser(fileInfo.getFileOriginName(), request);
        Resource resource = uploader.downloadResource(fileInfo.S3key());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    protected String getFileNameByBrowser(String fileName, HttpServletRequest request)
            throws UnsupportedEncodingException {
        String browser= "";
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if(headerName.equals("user-agent")) {
                browser = request.getHeader(headerName);
            }
        }

        String docName = "";
        if (browser.contains("Trident") || browser.contains("MSIE") || browser.contains("Edge")) {
            docName = mappingSpecialCharacter(URLEncoder.encode(fileName, "UTF-8"));

        } else if (browser.contains("Firefox")) {
            docName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        } else if (browser.contains("Opera")) {
            docName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        } else if (browser.contains("Chrome")) {
            docName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        } else if (browser.contains("Safari")) {
            docName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        } else {
            //throw new RuntimeException("미지원 브라우저");
        }
        return docName;
    }

    public static String mappingSpecialCharacter(String name) {

        // 파일명에 사용되는 특수문자
        char[] sh_list = { '~', '!', '@', '#', '$', '%', '&', '(', ')', '=', ';', '[', ']', '{', '}', '^', '-' };
        try {
            for (char sh : sh_list) {
                String encodeStr = URLEncoder.encode(sh + "", "UTF-8");
                name = name.replaceAll(encodeStr, "\\" + sh);
            }

            // 띄워쓰기 -> + 치환
            name = name.replaceAll("%2B", "+");
            // 콤마 -> _ 치환
            name = name.replaceAll("%2C", "_");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }
}
