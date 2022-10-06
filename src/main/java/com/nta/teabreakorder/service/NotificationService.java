package com.nta.teabreakorder.service;

import com.nta.teabreakorder.enums.Message;
import com.nta.teabreakorder.model.*;
import com.nta.teabreakorder.payload.response.ActionNotification;
import com.nta.teabreakorder.payload.response.TotalBill;
import com.nta.teabreakorder.repository.NotificationRepository;
import com.nta.teabreakorder.repository.NotificationUserRepository;
import com.nta.teabreakorder.repository.UserRepository;
import com.nta.teabreakorder.service.impl.PusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Autowired
    private NotificationUserService notificationUserService;

    @Autowired
    private PusherService pusherService;

    @Autowired
    private UserRepository userRepository;

    public void save(Notification notification){
        notificationRepository.save(notification);
    }

    public Notification get(Long id){
        return notificationRepository.findById(id).get();
    }

    public void doneNotification(Order order, List<OrderDetail> orderDetailList) throws IOException {
        this.saveNotification(order, orderDetailList, Message.DONE);
    }

    public void warningNotification(Order order, List<OrderDetail> orderDetailList) throws IOException {
        this.saveNotification(order, orderDetailList, Message.WARNING);
    }

    public void saveNotification(Order order, List<OrderDetail> orderDetailList, Message message) throws IOException {
        Notification notification = new Notification();
        Long countNotification = notificationRepository.existsByOrder(order.getId(), message);
        if (countNotification == 0) {
            notification.setMessage(message);
            notification.setOrder(order);
            this.save(notification);
            this.insertNotificationUser(order, orderDetailList, notification, message);
        } else {
            notification = notificationRepository.getByOrder(order.getId(), message);
            this.insertNotificationUser(order, orderDetailList, notification, message);
        }
    }

    public void insertNotificationUser(Order order, List<OrderDetail> orderDetailList, Notification notification, Message message) throws IOException {
        List<NotificationUser> notificationUserList = new ArrayList<>();
        for (OrderDetail element : orderDetailList) {
            Long countNotificationUser = notificationUserRepository.existsByNotification(notification.getId(), element.getId());
            if(countNotificationUser == 0) {
                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setUser(element.getUser());
                notificationUser.setRead(false);
                notificationUser.setNotification(notification);
                notificationUser.setProductName(element.getProduct().getName());
                notificationUser.setPhoto(element.getProduct().getPhotoList().get(0).getValue());
                notificationUser.setCreatedAt(LocalDateTime.now());
                notificationUser.setStore_name(order.getStore().getName());
                notificationUser.setOrder_detail(element.getId());
                notificationUserList.add(notificationUser);
                String content = "The order ";
                if(message == Message.DONE) {
                    content += element.getProduct().getName() + " is closed";
                } else {
                    content += element.getProduct().getName() + " don't confirm. Please confirm";
                }
                pusherService.sendToBrowserNotify(new ArrayList<>(){{add(element.getUser().getUsername());}},message.toString(),content, element.getProduct().getPhotoList().get(0).getValue());
            }
        }
        notificationUserRepository.saveAll(notificationUserList);
        if(notificationUserList.size() != 0) {
            this.pushNotification(notification);

        }
    }

    public void pushNotification (Notification notification){
        List<NotificationUser> notificationUsers = notificationUserService.findByNotification(notification);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        for (NotificationUser element : notificationUsers) {
            ActionNotification actionNotification = new ActionNotification();
            actionNotification.setNotification_id(notification.getId());
            actionNotification.setMessage(notification.getMessage());
            actionNotification.setOrder_id(notification.getOrder().getId());
            actionNotification.setNotification_user_id(element.getId());
            actionNotification.setIs_read(element.isRead());
            actionNotification.setUser_id(element.getUser().getId());
            actionNotification.setProduct_name(element.getProductName());
            actionNotification.setPhoto(element.getPhoto());
            actionNotification.setCreated_at( element.getCreatedAt().format(formatter));
            actionNotification.setStore_name(element.getStore_name());
            actionNotification.setOrder_detail(element.getOrder_detail());
            actionNotification.setTotal(element.getTotal());
            pusherService.triggerNotification(actionNotification);
        }
    }

    public void deleteNotications(List<Long> idOrders) {
        idOrders.forEach(id -> {
            Order order = new Order();
            order.setId(id);
            notificationRepository.deleteByOrder(order);
        });
    }

    public void billNotification(Order order, TotalBill res) throws IOException {
        Notification notification = new Notification();
        Long countNotification = notificationRepository.existsByOrder(order.getId(), Message.TIMEOUT);
        Optional<User> user = userRepository.findByUsername(res.getCreatedBy());
        String content = "";
        if(countNotification == 0){
            notification.setMessage(Message.TIMEOUT);
            notification.setOrder(order);
            this.save(notification);
            List<NotificationUser> notificationUserList = notificationUserRepository.findByNotification(notification);
            if(notificationUserList.size() == 0) {
                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setUser(user.get());
                notificationUser.setRead(false);
                notificationUser.setNotification(notification);
                notificationUser.setCreatedAt(LocalDateTime.now());
                notificationUser.setStore_name(order.getStore().getName());
                notificationUser.setTotal(res.getTotalAll().toString());
                notificationUserRepository.save(notificationUser);
                content += "Your order at the store "+ notificationUser.getStore_name() + " is time out. Total bill you should payment is  "
                        + res.getTotalAll().toString() + "đ";
                pusherService.sendToBrowserNotify(new ArrayList<>(){{add(user.get().getUsername());}},Message.TIMEOUT.toString(),content,
                        "https://thumbs.dreamstime.com/b/job-done-stamp-round-grunge-sign-label-181923208.jpg");
            }
        } else {
            notification = notificationRepository.getByOrder(order.getId(), Message.TIMEOUT);
            List<NotificationUser> notificationUserList = notificationUserRepository.findByNotification(notification);
            if(notificationUserList.size() != 0) {
                for (NotificationUser element : notificationUserList) {
                    element.setRead(false);
                    element.setTotal(res.getTotalAll().toString());
                    element.setCreatedAt(LocalDateTime.now());
                    notificationUserRepository.save(element);

                    content += "Your order at the store "+ element.getStore_name() + " is time out. Total bill you should payment is  "
                            + res.getTotalAll().toString() + "đ";
                    pusherService.sendToBrowserNotify(new ArrayList<>(){{add(user.get().getUsername());}},Message.TIMEOUT.toString(),content,
                            "https://thumbs.dreamstime.com/b/job-done-stamp-round-grunge-sign-label-181923208.jpg");
                }
            }
        }
        this.pushNotification(notification);
    }
}
