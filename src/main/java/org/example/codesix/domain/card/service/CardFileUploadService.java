package org.example.codesix.domain.card.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardFile;
import org.example.codesix.domain.card.model.Extension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CardFileUploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * 파일 업로드 메서드
     */
    public String uploadFile(MultipartFile file) throws IOException {
        validateFile(file); // 파일 유효성 검사

        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = generateUniqueFileName(originalFilename); // 고유 파일 이름 생성

        // 파일 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, uniqueFilename, file.getInputStream(), metadata);

        // S3 URL 반환
        return amazonS3.getUrl(bucket, uniqueFilename).toString();
    }

    /**
     * 파일 유효성 검사
     */
    private void validateFile(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename());
        // Enum으로 확장자 검사
        if (!Extension.isValidExtension(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + extension);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 5MB를 초과할 수 없습니다.");
        }
    }

    /**
     * 파일 삭제 메서드
     */
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1); // URL에서 파일 이름 추출
        amazonS3.deleteObject(bucket, fileName);
    }

    /**
     * 파일 확장자 추출
     */
    public String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * 고유 파일 이름 생성
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        String uniqueId = java.util.UUID.randomUUID().toString(); // 고유 ID 생성
        return uniqueId + "." + extension; // "고유ID.확장자" 형식으로 파일 이름 반환
    }

    public CardFile uploadFileAndSaveMetadata(Card card, MultipartFile file) throws IOException {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 없습니다.");
        }

        String uniqueFilename = generateUniqueFileName(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, uniqueFilename, file.getInputStream(), metadata);
        String fileUrl = amazonS3.getUrl(bucket, uniqueFilename).toString();

        return new CardFile(
                card,
                fileUrl,
                file.getSize(),
                getExtension(originalFilename),
                originalFilename // 반드시 파일 이름 설정
        );
    }
}
