package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.BankingHistory;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.OrderDetail;
import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.repository.BankingHistoryRepository;
import com.nta.teabreakorder.repository.OrderDetailRepository;
import com.nta.teabreakorder.repository.OrderRepository;
import com.nta.teabreakorder.repository.UserRepository;
import com.nta.teabreakorder.repository.dao.OrderDao;
import com.nta.teabreakorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BankingHistoryRepository bankingHistoryRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(orderDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
        return CommonUtil.createResponseEntityOK(order);
    }

    @Override
    public ResponseEntity create(Order order) throws Exception {
        //order.setId(0L);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        order.setUser(user);
        return CommonUtil.createResponseEntityOK(orderRepository.save(order));
    }

    @Override
    public ResponseEntity update(Order order) throws Exception {
        Order oldOrder = orderRepository.findById(order.getId()).orElseThrow(()->new Exception("NOT FOUND"));
        if(oldOrder.getUser() != null) {
            order.setUser(oldOrder.getUser());
        }
        return CommonUtil.createResponseEntityOK(orderRepository.save(order));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        List<Order> orderList = orderRepository.getAllByIds(ids);
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<BankingHistory> bankingHistories = new ArrayList<>();
        orderList.forEach(ob -> {
            orderDetails.addAll(ob.getOrderDetailList());
            ob.getOrderDetailList().clear();
        });

        orderDetails.forEach(ob->bankingHistories.addAll(ob.getBankingHistory()));
        bankingHistoryRepository.deleteAll(bankingHistories);

        orderDetailRepository.deleteAll(orderDetails);
        orderRepository.deleteAll(orderList);
        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity getTotalBill(String id) throws Exception {
        return CommonUtil.createResponseEntityOK(orderDao.getTotalBill(id));
    }

    @Override
    public List<Order> findByTimeRemainingOut(LocalDateTime localDateTime) throws Exception {
        return orderDao.findByTimeRemainingOut(localDateTime);
    }

    @Override
    public List<Order> findCreateByOrder(LocalDateTime localDateTime) throws Exception {
        return orderDao.findCreateByOrder(localDateTime);
    }


}
