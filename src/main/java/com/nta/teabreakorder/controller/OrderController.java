package com.nta.teabreakorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.OrderDetail;
import com.nta.teabreakorder.repository.NotificationRepository;
import com.nta.teabreakorder.service.NotificationService;
import com.nta.teabreakorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationService notificationService;

    private ObjectMapper objectMapper = CommonUtil.getObjectMapper();

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return orderService.get(pageable);
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) throws Exception {
        return orderService.getById(id);
    }
    @PreAuthorize("hasRole('ORDERER')")
    @PostMapping("")
    public ResponseEntity create(@RequestBody Order order) throws Exception {
        return orderService.create(order);
    }
    @PreAuthorize("hasRole('ORDERER')")
    @PutMapping("")
    public ResponseEntity put(@RequestBody Order order) throws Exception {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        if (order.getStatus() == Status.DONE) {
            List<OrderDetail> list = order.getOrderDetailList();
            for (OrderDetail od : list) {
                if(od.getStatus() == Status.ACTIVATED){
                    orderDetailList.add(od);
                }
            }
            notificationService.doneNotification(order, orderDetailList);
        }
        ResponseEntity res = orderService.update(order);
        return res;
    }
    @PreAuthorize("hasRole('ORDERER')")
    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        notificationService.deleteNotications(ids);
        return orderService.deletes(ids);
    }
    @PreAuthorize("hasRole('ORDERER')")
    @GetMapping("/totalBill")
    public ResponseEntity getTotalBill(@RequestParam String id ) throws Exception {
        return orderService.getTotalBill(id);
    }
}
