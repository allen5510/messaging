package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByAccount(String account);

    boolean existsByAccount(String account);

    @Modifying
    @Query("UPDATE user u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(@Param("id") long id, @Param("username") String username);

}
