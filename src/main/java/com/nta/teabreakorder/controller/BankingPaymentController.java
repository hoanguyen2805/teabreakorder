package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.model.BankingPaymentInfo;
import com.nta.teabreakorder.service.BankingPaymentInfoService;
import com.nta.teabreakorder.service.BankingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class BankingPaymentController {

    @Autowired
    private BankingPaymentInfoService bankingPaymentInfoService;

    @GetMapping("")
    public ResponseEntity getAll() throws Exception {
        return bankingPaymentInfoService.getAll();
    }

    @GetMapping("/by-order-id")
    public ResponseEntity getAllByOrderId(@RequestParam("orderId") Long id) throws Exception {
        return bankingPaymentInfoService.getAllByOrderId(id);
    }

    @PostMapping("")
    public ResponseEntity addPayment(@RequestBody BankingPaymentInfo bankingPaymentInfo) throws Exception {
        return bankingPaymentInfoService.create(bankingPaymentInfo);
    }


    @PutMapping("")
    public ResponseEntity updatePayment(@RequestBody BankingPaymentInfo bankingPaymentInfo) throws Exception {
        return bankingPaymentInfoService.update(bankingPaymentInfo);
    }

    @DeleteMapping("")
    public ResponseEntity delete(@RequestBody List<Long> ids) throws Exception {
        return bankingPaymentInfoService.deletes(ids);
    }
}
