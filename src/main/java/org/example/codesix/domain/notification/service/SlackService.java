package org.example.codesix.domain.notification.service;

import org.springframework.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.notification.entity.Notification;
import org.example.codesix.domain.notification.repository.NotificationRepository;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final RestTemplate restTemplate;
    private final NotificationRepository notificationRepository;

    public void callSlackApi(String location, String subject, Type type, Workspace workspace) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + workspace.getOAuthToken());
        String SLACK_API_URL = "https://slack.com/api/chat.postMessage";
        String message = location + "의 " + subject + " " + type.getMessage();
        String url = SLACK_API_URL + "?channel=" + workspace.getNotificationChannel() + "&text=" + message;
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        Notification notification = new Notification(workspace, type, message);
        notificationRepository.save(notification);
    }
}

