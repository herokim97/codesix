package org.example.codesix.domain.workspace.enums;

public enum NotificationPlatform {
    SLACK("slack"), DISCORD("discord");

    private final String name;

    NotificationPlatform(String name) {
        this.name = name;
    }
}