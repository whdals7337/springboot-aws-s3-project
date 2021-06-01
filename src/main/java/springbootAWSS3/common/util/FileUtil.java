package springbootAWSS3.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {

    // 파일 이름이 기준 length 보다 길경우 잘라서 리턴 메서드
    public static String cutFileName (String fileName, int length) {
        if(fileName.length() <= length) {
            return fileName;
        }

        String extension = fileName.substring(fileName.lastIndexOf("."));
        return fileName.substring(0, length - extension.length()) + extension;
    }

    // 랜덤 파일 이름 리턴 메서드
    public static String getRandomFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        return RandomStringUtils.randomAlphabetic(5)+ "_" + RandomStringUtils.randomNumeric(5) + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + extension;
    }
}
