package org.example.codesix.domain.workspace.dto;

import lombok.Getter;
import org.example.codesix.domain.workspace.enums.NotificationPlatform;

@Getter
public class WorkspaceNotificationRequestDto {

//    private final NotificationPlatform;     //이 프로젝트에서는 슬랙으로 고정

    private final String oAuthToken;

    private final String notificationChannel;

    public WorkspaceNotificationRequestDto(String oAuthToken, String notificationChannel) {
        this.oAuthToken = oAuthToken;
        this.notificationChannel = notificationChannel;
    }
}
