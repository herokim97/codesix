package org.example.codesix.domain.workspace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.global.entity.BaseEntity;

@Entity
@Getter
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "workspace_id"}))
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private Part part;

    public Member( Workspace workspace,User user, Part part) {
        this.user = user;
        this.workspace = workspace;
        this.part = part;
    }

    public Member() {
    }

    public void updatePart (Part part) {
        this.part = part;
    }
}
