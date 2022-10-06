package com.nta.teabreakorder.service;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.BankingPaymentInfo;
import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.repository.BankingPaymentInfoRepository;
import com.nta.teabreakorder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankingServiceImpl implements BankingPaymentInfoService {

    @Autowired
    private BankingPaymentInfoRepository bankingPaymentInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return null;
    }



    @Override
    public ResponseEntity getAll() throws Exception {
        List<BankingPaymentInfo> list = bankingPaymentInfoRepository.getAllByUser(getUserFromAuthentication().getId());
        return CommonUtil.createResponseEntityOK(list);
    }

    @Override
    public ResponseEntity getAllByOrderId(Long id) {
        List<BankingPaymentInfo> list = bankingPaymentInfoRepository.getAllByOrderId(id);
        return CommonUtil.createResponseEntityOK(list);
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity create(BankingPaymentInfo bankingPaymentInfo) throws Exception {
        User user = getUserFromAuthentication();
        user.addBankingPayment(bankingPaymentInfo);
        return CommonUtil.createResponseEntityOK(userRepository.save(user));
    }

    @Override
    public ResponseEntity update(BankingPaymentInfo bankingPaymentInfo) throws Exception {
        return CommonUtil.createResponseEntityOK(bankingPaymentInfoRepository.save(bankingPaymentInfo));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        bankingPaymentInfoRepository.deleteByUserIdAndId(getUserFromAuthentication().getId(),ids.get(0));
        return CommonUtil.createResponseEntityOK(1);
    }

    private User getUserFromAuthentication() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new Exception("User Not found"));

    }

}
