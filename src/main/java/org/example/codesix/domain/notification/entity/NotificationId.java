package org.example.codesix.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.workspace.entity.Workspace;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class NotificationId implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    public NotificationId(Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return  false;
        }
        NotificationId notificationId = (NotificationId) o;
        return Objects.equals(id, notificationId.id) && Objects.equals(workspace, notificationId.workspace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workspace);
    }
}
