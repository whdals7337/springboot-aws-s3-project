package springbootAWSS3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springbootAWSS3.domain.Entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
