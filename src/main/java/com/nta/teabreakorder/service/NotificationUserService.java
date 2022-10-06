package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.NotificationUser;
import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.repository.NotificationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationUserService {
    @Autowired
    private NotificationUserRepository notificationUserRepository;

    public void save(NotificationUser notificationUser) {
        notificationUserRepository.save(notificationUser);
    }

    public List<NotificationUser> findByNotification(Notification notification){
        return notificationUserRepository.findByNotification(notification);
    }

    public List<NotificationUser> findByUserOrderByCreatedAtDesc(User user) {
        return notificationUserRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public NotificationUser findById(Long id) {
        return notificationUserRepository.findById(id).get();
    }

    public ResponseEntity<?> deleteNoticationUser(Long id) {
        notificationUserRepository.deleteById(id);
        return null;
    }
}
