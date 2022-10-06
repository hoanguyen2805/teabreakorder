package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Modifying
    @Transactional
    @Query("update Product set isDeleted = true where id in :ids")
    void deletes(@Param("ids") List<Long> ids);
}
