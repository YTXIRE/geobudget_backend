package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FcmService {

    @Autowired
    private UserRepository userRepository;

    @Value("${firebase.credentials.path:classpath:firebase-service-account.json}")
    private String credentialsPath;

    private boolean isInitialized = false;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(
                    credentialsPath.replace("classpath:", "")
                );
                if (serviceAccount != null) {
                    FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                    FirebaseApp.initializeApp(options);
                    isInitialized = true;
                    log.info("Firebase FCM initialized successfully");
                } else {
                    log.warn("Firebase credentials file not found at: {}, FCM disabled", credentialsPath);
                }
            }
        } catch (Exception e) {
            log.warn("Firebase FCM initialization failed: {}. FCM disabled.", e.getMessage());
        }
    }

    public void sendNotification(Long userId, String title, String body) {
        if (!isInitialized) return;
        
        String fcmToken = getFcmToken(userId);
        if (fcmToken == null || fcmToken.isEmpty()) return;

        try {
            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .build();
            
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Notification sent to user {}: {}", userId, response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to send notification to user {}: {}", userId, e.getMessage());
        }
    }

    public void sendDataNotification(Long userId, String title, String body, java.util.Map<String, String> data) {
        if (!isInitialized) return;
        
        String fcmToken = getFcmToken(userId);
        if (fcmToken == null || fcmToken.isEmpty()) return;

        try {
            Message.Builder messageBuilder = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build());
            
            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }
            
            Message message = messageBuilder.build();
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to send data notification to user {}: {}", userId, e.getMessage());
        }
    }

    public void saveToken(Long userId, String token) {
        if (token == null || token.isEmpty()) return;
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFcmToken(token);
            userRepository.save(user);
            log.info("FCM token saved for user {}", userId);
        }
    }

    public void deleteToken(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFcmToken(null);
            userRepository.save(user);
        }
    }

    private String getFcmToken(Long userId) {
        return userRepository.findById(userId)
            .map(User::getFcmToken)
            .orElse(null);
    }
}
