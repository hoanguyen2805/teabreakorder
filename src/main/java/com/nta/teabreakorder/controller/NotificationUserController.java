package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.payload.response.ActionNotification;
import com.nta.teabreakorder.service.NotificationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/notification_user")
public class NotificationUserController {

    @Autowired
    private NotificationUserService notificationUserService;

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@RequestBody ActionNotification notification) throws Exception {
        return notificationUserService.deleteNoticationUser(notification.getNotification_user_id());
    }
}
