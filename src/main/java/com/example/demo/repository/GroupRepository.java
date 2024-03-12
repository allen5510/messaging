package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entity.GroupEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    @Modifying
    @Query("UPDATE group_entity e SET e.ownerId = :owner_id WHERE e.id = :id")
    void updateOwnerById(@Param("id") long id, @Param("owner_id") long ownerId);
}
