package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.exception.ResourceNotFoundException;
import com.nta.teabreakorder.model.NotificationUser;
import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.payload.response.ActionNotification;
import com.nta.teabreakorder.payload.response.MessageResponse;
import com.nta.teabreakorder.security.jwt.JwtUtils;
import com.nta.teabreakorder.service.NotificationUserService;
import com.nta.teabreakorder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationUserService notificationUserService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ActionNotification>> getNotificationsByUser(HttpServletRequest request) throws ResourceNotFoundException {

        User user = userService.getUserByToken(request);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        List<NotificationUser> notificationUserList = notificationUserService.findByUserOrderByCreatedAtDesc(user);
        List<ActionNotification> actionNotifications = new ArrayList<>();
        notificationUserList.forEach(element -> {
            ActionNotification actionNotification = new ActionNotification();
            actionNotification.setNotification_id(element.getNotification().getId());
            actionNotification.setMessage(element.getNotification().getMessage());
            actionNotification.setOrder_id(element.getNotification().getOrder().getId());
            actionNotification.setNotification_user_id(element.getId());
            actionNotification.setIs_read(element.isRead());
            actionNotification.setUser_id(element.getUser().getId());
            actionNotification.setProduct_name(element.getProductName());
            actionNotification.setPhoto(element.getPhoto());
            actionNotification.setCreated_at(element.getCreatedAt().format(formatter));
            actionNotification.setStore_name(element.getStore_name());
            actionNotification.setTotal(element.getTotal());
            actionNotifications.add(actionNotification);
            actionNotification.setUser(element.getUser());
            actionNotification.setSender(element.getSender());
        });
        return new ResponseEntity<>(actionNotifications, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public ResponseEntity updateIsReadTrue(@RequestBody List<ActionNotification> actionNotifications) throws Exception {
        actionNotifications.forEach(element -> {
            NotificationUser notificationUser = notificationUserService.findById(element.getNotification_user_id());
            notificationUser.setRead(true);
            notificationUserService.save(notificationUser);
        });
        return ResponseEntity.ok(new MessageResponse("Cập nhật thông báo đã đọc thành công!"));
    }
}
