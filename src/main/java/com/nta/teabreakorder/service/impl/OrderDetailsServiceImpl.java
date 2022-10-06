package com.nta.teabreakorder.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.enums.BankingStatus;
import com.nta.teabreakorder.enums.Message;
import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.model.*;
import com.nta.teabreakorder.payload.response.ActionEvent;
import com.nta.teabreakorder.payload.response.ActionNotification;
import com.nta.teabreakorder.repository.*;
import com.nta.teabreakorder.repository.dao.OrderDetailDao;
import com.nta.teabreakorder.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailsServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private PusherService pusherService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = CommonUtil.getObjectMapper();

    @Autowired
    private BankingHistoryRepository bankingHistoryRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {

        return CommonUtil.createResponseEntityOK(orderDetailDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
        return CommonUtil.createResponseEntityOK(orderDetail);
    }

    @Override
    public ResponseEntity create(OrderDetail orderDetail) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderDetail oldOrder = orderDetailRepository.getAllByOrderIdAndOrderStatusAndUserNameAndProductId(orderDetail.getOrder().getId(), Status.ACTIVATED, username, orderDetail.getProduct().getId());

        if (oldOrder != null) {
            oldOrder.setQuantity((byte) (oldOrder.getQuantity() + orderDetail.getQuantity()));
            orderDetailRepository.save(oldOrder);
            pusherService.triggerEvent(PusherService.ORDERS + "/" + oldOrder.getOrder().getId(), new ActionEvent(username, PusherService.EDIT_ORDER, objectMapper.writeValueAsString(oldOrder)));
            return CommonUtil.createResponseEntityOK(oldOrder);
        } else {
            OrderDetail order = orderDetailRepository.save(orderDetail);
            pusherService.triggerEvent(PusherService.ORDERS + "/" + order.getOrder().getId(), new ActionEvent(username, PusherService.ADD_ORDER, objectMapper.writeValueAsString(order)));
            return CommonUtil.createResponseEntityOK(order);
        }
    }

    @Override
    public ResponseEntity update(OrderDetail orderDetail) throws Exception {
        OrderDetail od = orderDetailRepository.save(orderDetail);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        pusherService.triggerEvent(PusherService.ORDERS + "/" + od.getOrder().getId(), new ActionEvent(username, PusherService.EDIT_ORDER, objectMapper.writeValueAsString(od)));
        return CommonUtil.createResponseEntityOK(od);
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        List<OrderDetail> orderDetails = orderDetailRepository.getByIds(ids);

        //updateList
        orderDetailRepository.saveAll(orderDetails.stream()
                .filter(ob -> !ob.getOrder().getStatus().equals(Status.ACTIVATED))
                .peek(orderDetail -> orderDetail.setDeleted(true))
                .collect(Collectors.toSet()));

        //deleteList
        orderDetailRepository.deleteAll(orderDetails.stream().filter(orderDetail -> orderDetail.getOrder().getStatus().equals(Status.ACTIVATED)).collect(Collectors.toSet()));

        Long orderId = orderDetails.get(0).getOrder().getId();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        pusherService.triggerEvent(PusherService.ORDERS + "/" + orderId, new ActionEvent(username, PusherService.REMOVE_ORDER, objectMapper.writeValueAsString(ids)));

        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity getOrderDetailsHistory() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return CommonUtil.createResponseEntityOK(orderDetailDao.getOrderDetailsHistory(username));
    }

    @Override
    public ResponseEntity createByList(List<OrderDetail> orderDetailList) throws Exception {
        if (orderDetailList.isEmpty()) throw new Exception("List empty");
        ResponseEntity resData = CommonUtil.createResponseEntityOK(orderDetailRepository.saveAll(orderDetailList));
        return resData;
    }

    @Override
    public ResponseEntity updateAll(List<OrderDetail> orderDetailList) throws Exception {
        List<OrderDetail> orderDetails = orderDetailRepository.saveAll(orderDetailList);

        Long orderId = orderDetails.get(0).getOrder().getId();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        pusherService.triggerEvent(PusherService.ORDERS + "/" + orderId, new ActionEvent(username, PusherService.EDIT_ORDERS, objectMapper.writeValueAsString(orderDetails)));

        return CommonUtil.createResponseEntityOK(orderDetailRepository.saveAll(orderDetailList));
    }

    @Override
    public ResponseEntity getCartByOrderId(Long orderId) throws Exception {
        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        return CommonUtil.createResponseEntityOK(orderDetailRepository.getAllByOrderIdAndCreatedByAndStatus(orderId, createdBy, Status.UNACTIVATED));
    }

    @Override
    public ResponseEntity getAllByPaidIsFalse() throws Exception {
        return CommonUtil.createResponseEntityOK(orderDetailRepository.getAllByPaidIsFalseAndUserId(getUserFromAuthentication().getId(), Status.ACTIVATED, Status.PENDINGPAYMENT));
    }

    @Override
    public ResponseEntity countAllByPaidIsFalse() throws Exception {
        return CommonUtil.createResponseEntityOK(orderDetailRepository.countAllByPaidIsFalseAndUserId(getUserFromAuthentication().getId(), Status.ACTIVATED, Status.PENDINGPAYMENT));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity requestedPayment(Long orderId) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(orderId).orElseThrow(() -> new Exception("OrderDetail not found"));
        User sender = getUserFromAuthentication();
        User createdOrder = userRepository.findByUsername(orderDetail.getOrder().getCreatedBy()).orElseThrow(() -> new Exception("User not foudn"));

        BankingHistory bankingHistory = new BankingHistory(0L, orderDetail, sender.getUsername(), LocalDateTime.now(), createdOrder.getUsername(), null, BankingStatus.PENDING);
        bankingHistoryRepository.save(bankingHistory);


        //Create notify
        Notification notification = new Notification(0L, Message.PAYMENT_REQUESTED, orderDetail.getOrder(), null);
        notification = notificationRepository.save(notification);

        long price = orderDetail.getProduct().getDiscountPrice() != null ? orderDetail.getProduct().getDiscountPrice().getValue() : orderDetail.getProduct().getPrice().getValue();

        long total = orderDetail.getQuantity() * price;

        NotificationUser notificationUser = new NotificationUser(0L, createdOrder, sender, notification, false, orderDetail.getProduct().getName(), orderDetail.getProduct().getPhotoList().get(0).getValue(), LocalDateTime
                .now(), orderDetail.getOrder().getStore().getName(), orderDetail.getId(), total + "");

        notificationUserRepository.save(notificationUser);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        ActionNotification actionNotification = new ActionNotification();
        actionNotification.setNotification_id(notificationUser.getNotification().getId());
        actionNotification.setMessage(notificationUser.getNotification().getMessage());
        actionNotification.setOrder_id(notificationUser.getNotification().getOrder().getId());
        actionNotification.setNotification_user_id(notificationUser.getId());
        actionNotification.setIs_read(notificationUser.isRead());
        actionNotification.setUser_id(notificationUser.getUser().getId());
        actionNotification.setProduct_name(notificationUser.getProductName());
        actionNotification.setPhoto(notificationUser.getPhoto());
        actionNotification.setCreated_at(notificationUser.getCreatedAt().format(formatter));
        actionNotification.setStore_name(notificationUser.getStore_name());
        actionNotification.setTotal(notificationUser.getTotal());
        actionNotification.setUser(notificationUser.getUser());
        actionNotification.setSender(notificationUser.getSender());
        pusherService.triggerNotification(actionNotification);

        return CommonUtil.createResponseEntityOK(orderDetailRepository.findById(orderId));
    }

    @Override
    public ResponseEntity approvedPayment(Long orderId, boolean approve) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(orderId).orElseThrow(() -> new Exception("OrderDetail not found"));

        Notification notification = null;
        User sender = getUserFromAuthentication();
        User receiver = userRepository.findByUsername(orderDetail.getCreatedBy()).orElseThrow(() -> new Exception("User not foudn"));


        if (approve) {
            orderDetail.getBankingHistory().get(0).setStatus(BankingStatus.APPROVED);
            orderDetail.getBankingHistory().get(0).setTimeApproved(LocalDateTime.now());
            orderDetail.setPaid(true);

            notification = new Notification(0L, Message.PAYMENT_APPROVED, orderDetail.getOrder(), null);
        } else {
            orderDetail.getBankingHistory().get(0).setStatus(BankingStatus.REJECTED);
            orderDetail.getBankingHistory().get(0).setTimeApproved(LocalDateTime.now());
            orderDetail.setPaid(false);

            notification = new Notification(0L, Message.PAYMENT_REJECTED, orderDetail.getOrder(), null);
        }

        orderDetailRepository.save(orderDetail);
        notification = notificationRepository.save(notification);
        long price = orderDetail.getProduct().getDiscountPrice() != null ? orderDetail.getProduct().getDiscountPrice().getValue() : orderDetail.getProduct().getPrice().getValue();
        long total = orderDetail.getQuantity() * price;

        NotificationUser notificationUser = new NotificationUser(0L, receiver, sender, notification, false, orderDetail.getProduct().getName(), orderDetail.getProduct().getPhotoList().get(0).getValue(), LocalDateTime
                .now(), orderDetail.getOrder().getStore().getName(), orderDetail.getId(), total + "");
        notificationUserRepository.save(notificationUser);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        ActionNotification actionNotification = new ActionNotification();
        actionNotification.setNotification_id(notificationUser.getNotification().getId());
        actionNotification.setMessage(notificationUser.getNotification().getMessage());
        actionNotification.setOrder_id(notificationUser.getNotification().getOrder().getId());
        actionNotification.setNotification_user_id(notificationUser.getId());
        actionNotification.setIs_read(notificationUser.isRead());
        actionNotification.setUser_id(notificationUser.getUser().getId());
        actionNotification.setProduct_name(notificationUser.getProductName());
        actionNotification.setPhoto(notificationUser.getPhoto());
        actionNotification.setCreated_at(notificationUser.getCreatedAt().format(formatter));
        actionNotification.setStore_name(notificationUser.getStore_name());
        actionNotification.setTotal(notificationUser.getTotal());
        actionNotification.setUser(notificationUser.getUser());
        actionNotification.setSender(notificationUser.getSender());
        pusherService.triggerNotification(actionNotification);

        return CommonUtil.createResponseEntityOK(orderDetail);
    }

    private User getUserFromAuthentication() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new Exception("User Not found"));

    }
}

