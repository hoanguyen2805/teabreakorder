package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.BankingPaymentInfo;
import com.nta.teabreakorder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BankingPaymentInfoRepository extends JpaRepository<BankingPaymentInfo, Long> {


    @Query(value = "select * from banking_payment where user_id = ?", nativeQuery = true)
    List<BankingPaymentInfo> getAllByUser(Long userId);

    @Transactional
    @Modifying
    @Query(value = "delete from banking_payment where user_id = ? and id=?", nativeQuery = true)
    void deleteByUserIdAndId(Long userId,Long id);

    @Query(value = "select bk.id, bk.account_name, bk.account_no, bk.bin, bk.code, bk.logo, bk.name, bk.short_name, bk.user_id from banking_payment bk inner join users u on u.id = bk.user_id\n" +
            "inner join orders o on u.id = o.user_id where o.id=?", nativeQuery = true)
    List<BankingPaymentInfo> getAllByOrderId(Long id);
}
