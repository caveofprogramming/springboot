package com.caveofprogramming.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.caveofprogramming.model.entity.Message;

@Repository
public interface MessageDao extends CrudRepository<Message, Long> {

	@Query("select m from Message m where (m.toUser.id=:toUser and m.fromUser.id=:fromUser) or (m.toUser.id=:fromUser and m.fromUser.id=:toUser) order by m.sent desc")
	Slice<Message> fetchConversation(@Param("toUser") Long toUser, @Param("fromUser") Long fromUser, Pageable pageable);

	Page<Message> findByToUserIdAndReadFalseOrderBySentDesc(Long toUserId, Pageable request);
}
