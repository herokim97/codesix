package org.example.codesix.domain.workspace.enums;

public enum Part {
    WORKSPACE("workspace"),BOARD("board"), READ("read");

    private final String name;

    Part(String name) {
        this.name = name;
    }
}
