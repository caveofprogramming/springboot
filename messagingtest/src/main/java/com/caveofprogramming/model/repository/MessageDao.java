package com.caveofprogramming.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import com.caveofprogramming.model.entity.Message;

public interface MessageDao extends CrudRepository<Message, Long> {

	Page<Message> findByToUserIdAndReadFalseOrderBySentDesc(Long toUser, Pageable request);
	Slice<Message> findByToUserIdAndFromUserIdOrderBySentDesc(Long toUser, Long fromUser, Pageable pageable);
}
