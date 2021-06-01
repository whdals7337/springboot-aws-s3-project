package springbootAWSS3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import springbootAWSS3.common.uploader.Uploader;
import springbootAWSS3.common.util.FileUtil;
import springbootAWSS3.domain.Entity.FileInfo;
import springbootAWSS3.repository.FileInfoRepository;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileInfoService {

    private final Uploader uploader;
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public FileInfo upload(@RequestParam("data") MultipartFile file) throws IOException{
        String fileUrl ="";
        try {
            fileUrl = uploader.upload(file, "testImage");
            FileInfo fileInfo = new FileInfo(FileUtil.cutFileName(file.getOriginalFilename(), 500), fileUrl);
            return fileInfoRepository.save(fileInfo);

        } catch (IOException ie) {
            log.info("S3파일 저장 중 예외 발생");
            throw ie;

        } catch (Exception e) {
            log.info("s3에 저장되었던 파일 삭제");
            uploader.delete(fileUrl.substring(fileUrl.lastIndexOf(".com/") + 5));
            throw e;
        }
    }

    @Transactional
    public Long delete(@PathVariable("id") Long id) {
        FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("존재 하지 않는 파일"));
        fileInfoRepository.deleteById(id);
        uploader.delete(fileInfo.S3key());
        return id;
    }
}
