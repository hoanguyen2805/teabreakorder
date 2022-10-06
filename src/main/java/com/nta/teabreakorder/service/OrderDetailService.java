package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.OrderDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderDetailService extends CommonService<OrderDetail> {
    ResponseEntity getOrderDetailsHistory() throws Exception;

    ResponseEntity createByList(List<OrderDetail> orderDetailList) throws Exception;

    ResponseEntity updateAll(List<OrderDetail> orderDetailList) throws Exception;

    ResponseEntity getCartByOrderId(Long orderId) throws Exception;

    ResponseEntity getAllByPaidIsFalse() throws Exception;

    ResponseEntity countAllByPaidIsFalse() throws Exception;

    ResponseEntity requestedPayment(Long orderId) throws Exception;

    ResponseEntity approvedPayment(Long orderId, boolean approve) throws Exception;


}
