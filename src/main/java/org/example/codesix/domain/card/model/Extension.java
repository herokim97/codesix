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

    public static boolean isValidExtension(String extension) {
        return Arrays.stream(Extension.values())
                .anyMatch(fileType -> fileType.getExtension().equalsIgnoreCase(extension));
    }
}