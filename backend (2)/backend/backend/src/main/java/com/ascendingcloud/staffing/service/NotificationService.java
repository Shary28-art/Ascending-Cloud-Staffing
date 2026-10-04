package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Notification;
import com.ascendingcloud.staffing.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(
            Integer id,
            Notification notification) {

        Notification existingNotification = getNotificationById(id);

        return notificationRepository.save(existingNotification);
    }

    public void deleteNotification(Integer id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }

        notificationRepository.deleteById(id);
    }
}