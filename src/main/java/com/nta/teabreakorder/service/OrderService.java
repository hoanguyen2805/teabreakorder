package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Order;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends CommonService<Order>{

    public ResponseEntity getTotalBill(String id) throws  Exception;

    public List<Order> findByTimeRemainingOut(LocalDateTime localDateTime) throws  Exception;

    public List<Order> findCreateByOrder(LocalDateTime localDateTime) throws  Exception;

}
