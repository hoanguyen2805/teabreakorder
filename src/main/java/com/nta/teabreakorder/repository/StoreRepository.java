package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByUrl(String url);

    @Query(value = "update Store set isDeleted = :status where id in :ids ")
    @Modifying
    @Transactional
    void setDeleted(@Param(value = "ids") List<Long> ids,
                    @Param(value = "status") boolean isDeleted);


    @Query(value = "from Store where id in :ids")
    List<Store> findAllByIds(@Param("ids") List<Long> ids);
}
