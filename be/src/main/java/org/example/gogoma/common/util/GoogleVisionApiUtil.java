package org.example.gogoma.common.util;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleVisionApiUtil {

    /**
     * URL에서 이미지를 가져와 OCR 처리
     */
    public String extractTextFromImageUrl(String imageUrl) {
        try {
            ByteString imgBytes = readImageFromUrl(imageUrl);
            return processImage(imgBytes);
        } catch (IOException e) {
            log.error("Failed to read the image from URL: {}", imageUrl, e);
            throw new BusinessException(ExceptionCode.BUSINESS_ERROR);
        }
    }

    /**
     * MultipartFile에서 이미지를 가져와 OCR 처리
     */
    public String extractTextFromImage(MultipartFile file) {
        try {
            ByteString imgBytes = ByteString.readFrom(file.getInputStream());
            return processImage(imgBytes);
        } catch (IOException e) {
            log.error("Failed to read the image file: {}", file.getOriginalFilename(), e);
            throw new BusinessException(ExceptionCode.BUSINESS_ERROR);
        }
    }

    /**
     * Google Vision AI를 사용하여 OCR 처리
     */
    private String processImage(ByteString imgBytes) {
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(Collections.singletonList(request));

            // OCR 결과 처리
            StringBuilder extractedText = new StringBuilder();
            for (AnnotateImageResponse res : response.getResponsesList()) {
                if (res.hasError()) {
                    log.error("Google Vision API Error: {}", res.getError().getMessage());
                    throw new BusinessException(ExceptionCode.BUSINESS_ERROR);
                }
                extractedText.append(res.getFullTextAnnotation().getText());
            }
            return extractedText.toString();
        } catch (IOException e) {
            log.error("Document AI processing failed.", e);
            throw new BusinessException(ExceptionCode.BUSINESS_ERROR);
        }
    }

    /**
     * URL에서 이미지 데이터 읽기
     */
    private ByteString readImageFromUrl(String imageUrl) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            return ByteString.readFrom(in);
        }
    }
}
