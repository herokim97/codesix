package org.example.codesix.domain.workspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.workspace.enums.NotificationPlatform;
import org.example.codesix.global.entity.BaseEntity;

import java.util.List;

@Getter
@Entity
@Table(name = "workspace")
@AllArgsConstructor
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Member> members;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @Enumerated(EnumType.STRING)
    private NotificationPlatform notificationPlatform = NotificationPlatform.SLACK;         //이 프로젝트에서는 SLACK을 통한 알림만을 구현

    private String oAuthToken;

    private String notificationChannel;

    public Workspace(String title, String description, User creator) {
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    public Workspace() {
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void updateNotificationSettings(String oAuthToken, String notificationChannel) {
        this.oAuthToken = oAuthToken;
        this.notificationChannel = notificationChannel;
    }
}
