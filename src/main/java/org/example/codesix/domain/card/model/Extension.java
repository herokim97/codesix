package org.example.codesix.domain.card.model;

import java.util.Arrays;
public enum Extension {
    JPG("jpg"),
    PNG("png"),
    PDF("pdf"),
    CSV("csv");
    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    // 파일 확장자 유효성 검사
    public static boolean isValidExtension(String extension) {
        return Arrays.stream(Extension.values())
                .anyMatch(fileType -> fileType.getExtension().equalsIgnoreCase(extension));
    }
}