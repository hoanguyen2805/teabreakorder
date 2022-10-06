package com.nta.teabreakorder.scheduler;

import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.model.*;
import com.nta.teabreakorder.payload.response.TotalBill;
import com.nta.teabreakorder.repository.OrderDetailRepository;
import com.nta.teabreakorder.repository.OrderRepository;
import com.nta.teabreakorder.repository.RoleRepository;
import com.nta.teabreakorder.repository.UserRepository;
import com.nta.teabreakorder.service.NotificationService;
import com.nta.teabreakorder.service.OrderService;
import com.nta.teabreakorder.service.impl.PusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SchedulerRole {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 */5 * * * *")
    public void cronJobSch() {
        List<User> users = new ArrayList<User>();
        users = userRepository.getUserWithTimeRemaining(LocalDateTime.now());
        users.forEach((user) -> {
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            user.setRoles(roles);
            user.setTimeRemaining(null);
            userRepository.save(user);
        });
        System.out.println("DELETE ROLE ORDERER");
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void cronJobOrderTimeOut() throws Exception {
        List<Order> orders = orderService.findByTimeRemainingOut(LocalDateTime.now());
        if(orders.size() != 0){
            for (Order order : orders) {
                List<OrderDetail> list = orderDetailRepository.getByOrderId(order.getId());
                List<OrderDetail> orderDetailList = new ArrayList<>();
                if(list.size() != 0) {
                    for (OrderDetail element : list) {
                        if (element.getStatus() != Status.ACTIVATED) {
                            orderDetailList.add(element);
                        }
                    }
                }
                notificationService.warningNotification(order, orderDetailList);
                System.out.println("WARNING TIME OUT ORDER");
            }
        }

        List<Order> ordersTimeOut = orderService.findCreateByOrder(LocalDateTime.now());
        if(ordersTimeOut.size() != 0){
            for (Order element : ordersTimeOut) {
                ResponseEntity result = orderService.getTotalBill(Long.toString(element.getId()));
                Map<String, TotalBill> data = (Map<String, TotalBill>) result.getBody();
                TotalBill res = data.get("data");
                notificationService.billNotification(element, res);
                System.out.println("NOTIFICATION TIME DONE ORDER");
            }
        }

    }
}