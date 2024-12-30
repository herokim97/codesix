package org.example.codesix.domain.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.global.entity.BaseEntity;

@Getter
@Entity
public class Notification extends BaseEntity {

    @EmbeddedId
    private NotificationId id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String message;

    public Notification(Workspace workspace, Type type, String message) {
        this.id = new NotificationId(workspace);
        this.type = type;
        this.message = message;
    }

    public Notification() {
    }
}

