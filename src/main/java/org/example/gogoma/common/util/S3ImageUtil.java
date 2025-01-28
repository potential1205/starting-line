package org.example.gogoma.common.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.config.S3Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ImageUtil {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        // 파일 이름에서 확장자 추출
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("유효하지 않은 파일 형식입니다.");
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String s3Key = folderName + "/" + UUID.randomUUID() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Config.amazonS3Client().putObject(
                    new PutObjectRequest(bucket, s3Key, inputStream, metadata)
            );
        }

        return s3Config.amazonS3Client().getUrl(bucket, s3Key).toString();
    }

    /**
     * S3에서 이미지 삭제
     * @param s3Url 실제 이미지의 URL (예: https://bucketname.s3.region.amazonaws.com/foldername/uuid.PNG)
     * S3에서 이미지를 삭제하려면 해당 파일의 "Key"가 필요합니다. 따라서 추출하는 메서드 필요: extracts3ImageKeyFromUrl
     */
    public void deleteImageByUrl(String imageUrl) {
        try {
            String s3ImageKey = extracts3ImageKeyFromUrl(imageUrl);

            s3Config.amazonS3Client().deleteObject(bucket, s3ImageKey);
        } catch (Exception e) {
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private String extracts3ImageKeyFromUrl(String imageUrl) {
        try {
            return imageUrl.substring(imageUrl.indexOf(".com/") + 5);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 S3 URL입니다: " + imageUrl, e);
        }
    }
}
