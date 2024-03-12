package com.example.demo.repository;

import java.util.List;

import com.example.demo.controller.util.ColumnNames;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.repository.entity.MessageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {

	@Query("SELECT m FROM message m " +
			"WHERE m.groupId=:group_id " +
			"AND m.sendAt > :send_after " +
			"AND m.sendAt > :joined_at " +
			"ORDER BY m.sendAt ASC")
	List<MessageEntity> queryNewerInRangeMessagesByGroupId(@Param(ColumnNames.GROUP_ID) long groupId, @Param("joined_at") long joinTimestamp, @Param("send_after") long sendAfter, Pageable pageable);

	@Query("SELECT m FROM message m " +
			"WHERE group_id=:group_id " +
			"AND send_at < :send_before " +
			"AND send_at > :joined_at " +
			"ORDER BY send_at DESC")
	List<MessageEntity> queryOlderInRangeMessagesByGroupId(@Param(ColumnNames.GROUP_ID) long groupId, @Param("joined_at") long joinTimestamp, @Param("send_before") long sendBefore, Pageable pageable);
}
