package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.enums.Message;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT count(n) FROM Notification n WHERE n.order.id =?1 and n.message =?2 ")
    long existsByOrder(Long id, Message message);

    @Modifying
    @Transactional
    long deleteByOrder(Order order);
    
    @Query(value = "SELECT n FROM Notification n WHERE n.order.id =?1 and n.message =?2 ")
    Notification getByOrder(Long id, Message message);


}
