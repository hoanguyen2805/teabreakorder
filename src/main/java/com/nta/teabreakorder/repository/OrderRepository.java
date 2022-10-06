package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query("update Order set isDeleted = true where id in :ids")
    void deletes(@Param("ids") List<Long> ids);

    @Query("select o from Order o where o.id in :ids")
    List<Order> getAllByIds(@Param("ids") List<Long> ids);

}
