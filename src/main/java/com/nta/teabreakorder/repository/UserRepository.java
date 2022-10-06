package com.nta.teabreakorder.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.nta.teabreakorder.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u From User u WHERE u.isDeleted = FALSE")
    Page<User> findAllByIsDeleted(Pageable pageable);

    @Query(value = "SELECT u From User u WHERE u.isDeleted = FALSE AND (u.username LIKE %:keyword% OR u.email LIKE %:keyword%)")
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update User u set u.isDeleted = true where u.id in :ids")
    void deletes(@Param("ids") List<Long> ids);

    @Query(value = "SELECT u FROM User u WHERE u.timeRemaining <= :strDate")
    List<User> getUserWithTimeRemaining(@Param("strDate") LocalDateTime strDate);



}