package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.enums.Message;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.NotificationUser;
import com.nta.teabreakorder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    List<NotificationUser> findByNotification(Notification notification);

    List<NotificationUser> findByUserOrderByCreatedAtDesc(User user);

    @Query(value = "SELECT count(n) FROM NotificationUser n WHERE n.notification.id =?1 and n.order_detail =?2 ")
    Long existsByNotification(Long notification_id, Long idOrderDetail);
}
