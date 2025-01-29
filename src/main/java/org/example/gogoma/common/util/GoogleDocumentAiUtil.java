package org.example.gogoma.common.util;

import com.google.cloud.documentai.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDocumentAiUtil {

    @Value("${google.document.ai.name}")
    private String name;

    /**
     * URL에서 이미지를 가져와서 OCR 처리
     */
    public String extractTextFromImageUrl(String imageUrl) {
        try {
            ByteString content = readImageFromUrl(imageUrl);
            return processDocument(content, "image/jpeg"); // MIME 타입 지정
        } catch (IOException e) {
            log.error("Failed to read the image from URL: {}", imageUrl, e);
            return "Error: Failed to read the image from the URL.";
        }
    }

    /**
     * MultipartFile에서 이미지를 가져와서 OCR 처리
     */
    public String extractTextFromImage(MultipartFile file) {
        try {
            ByteString content = ByteString.readFrom(file.getInputStream());
            return processDocument(content, file.getContentType());
        } catch (IOException e) {
            log.error("Failed to read the image file: {}", file.getOriginalFilename(), e);
            return "Error: Failed to read the image file.";
        }
    }

    /**
     * Document AI를 사용하여 OCR 처리
     */
    private String processDocument(ByteString content, String mimeType) {
        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create()) {
            RawDocument rawDocument = RawDocument.newBuilder()
                    .setContent(content)
                    .setMimeType(mimeType)
                    .build();

            ProcessRequest request = ProcessRequest.newBuilder()
                    .setName(name)
                    .setRawDocument(rawDocument)
                    .build();

            ProcessResponse response = client.processDocument(request);
            return response.getDocument().getText();
        } catch (IOException e) {
            log.error("Document AI processing failed.", e);
            return "Error: Document AI processing failed.";
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
