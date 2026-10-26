package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);

    @Query("select count(n) from Notification n "
            + "where n.user.id = :userId and n.isRead = false")
    long countUnread(@Param("userId") Integer userId);

    @Modifying
    @Query("update Notification n set n.isRead = true "
            + "where n.user.id = :userId and n.isRead = false")
    int markAllRead(@Param("userId") Integer userId);
}
