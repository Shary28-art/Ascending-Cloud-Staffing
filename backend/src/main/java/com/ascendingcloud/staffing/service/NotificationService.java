package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.NotificationDTO;
import com.ascendingcloud.staffing.entity.Notification;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.NotificationRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /** Creates a notification for a user (used by other services). */
    @Transactional
    public void notify(User user, String title, String message) {
        if (user == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getMyNotifications(User user) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countUnread(User user) {
        return notificationRepository.countUnread(user.getId());
    }

    @Transactional
    public NotificationDTO markRead(User user, Integer id) {
        Notification notification = getOwned(user, id);
        notification.setIsRead(true);

        return toDTO(notificationRepository.save(notification));
    }

    @Transactional
    public int markAllRead(User user) {
        return notificationRepository.markAllRead(user.getId());
    }

    @Transactional
    public void delete(User user, Integer id) {
        notificationRepository.delete(getOwned(user, id));
    }

    private Notification getOwned(User user, Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("This notification belongs to another user");
        }

        return notification;
    }

    private NotificationDTO toDTO(Notification n) {
        return new NotificationDTO(
                n.getId(), n.getTitle(), n.getMessage(), n.getIsRead(), n.getCreatedAt());
    }
}
