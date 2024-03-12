package com.example.demo.repository;

import com.example.demo.controller.util.ColumnNames;
import com.example.demo.repository.entity.type.JoinStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entity.GroupMemberEntity;
import com.example.demo.repository.entity.GroupMemberId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, GroupMemberId>{

    @Query("SELECT COUNT(gm) FROM group_member gm " +
            "WHERE gm.group.id = :group_id " +
            "AND gm.joinStatus <> com.example.demo.repository.entity.type.JoinStatus.EXIT")
    int countMembersByGroupId(@Param("group_id") long groupId);

    @Query("SELECT gm FROM group_member gm WHERE gm.group.id=:group_id")
    Set<GroupMemberEntity> queryMembersByGroupId(@Param(ColumnNames.GROUP_ID)long groupId);

    @Query("SELECT gm FROM group_member gm " +
            "WHERE gm.user.id = :user_id " +
            "AND gm.joinStatus <> com.example.demo.repository.entity.type.JoinStatus.EXIT " +
            "AND gm.group.updatedAt > :updated_after "+
            "ORDER BY gm.group.updatedAt ASC")
    List<GroupMemberEntity> queryGroupsByUserId(@Param("user_id") long userId,
                                                @Param("updated_after") long updatedAfter,
                                                Pageable pageable);

    @Modifying
    @Query("UPDATE group_member gm " +
            "SET gm.joinStatus=:join_status, gm.updatedAt=:current " +
            "WHERE gm.user.id=:user_id AND gm.group.id=:group_id")
    void updateStatus(@Param(ColumnNames.USER_ID)long userId,
                      @Param(ColumnNames.GROUP_ID)long groupId,
                      @Param(ColumnNames.MEMBER_JOIN_STATUS) JoinStatus joinStatus,
                      @Param("current")long currentTimestamp);

    @Modifying
    @Query("UPDATE group_member gm " +
            "SET gm.readAt=:read_at " +
            "WHERE gm.user.id=:user_id AND gm.group.id=:group_id")
    void updateReadTimestamp(@Param(ColumnNames.USER_ID)long userId, @Param(ColumnNames.GROUP_ID)long groupId, @Param(ColumnNames.MEMBER_READ_AT)long readTimestamp);

    @Modifying
    @Query(value="INSERT INTO group_member (user_id, group_id, join_status, joined_at, updated_at, read_at) VALUES (:user_id, :group_id, :join_status, -1, :current, :current)", nativeQuery=true)
    void createGroupMember(@Param(ColumnNames.USER_ID)long userId, @Param(ColumnNames.GROUP_ID)long groupId, @Param(ColumnNames.MEMBER_JOIN_STATUS)int joinStatus, @Param("current")long currentTimestamp);

    @Modifying
    @Query("UPDATE group_member gm SET gm.joinedAt=:current, gm.updatedAt =:current, gm.joinStatus=1 WHERE gm.user.id=:user_id AND group.id=:group_id")
    void setStatusJoined(@Param(ColumnNames.USER_ID)long userId, @Param(ColumnNames.GROUP_ID)long groupId, @Param("current")long currentTimestamp);

}
