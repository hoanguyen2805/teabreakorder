package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.BankingPaymentInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankingPaymentInfoService extends CommonService<BankingPaymentInfo> {

    ResponseEntity getAll() throws Exception;

    ResponseEntity getAllByOrderId(Long id);
}
