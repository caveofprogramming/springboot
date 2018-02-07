package com.caveofprogramming.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.caveofprogramming.model.entity.Message;

public interface MessageDao extends CrudRepository<Message, Long> {
	//Page<Message> findByToUserIdOrderBySentDesc(Long toUser, Pageable request);
	Page<Message> findDistinctByToUserIdOrderBySentDesc(Long toUser, Pageable request);
	
	List<Message> findByToUserIdAndFromUserIdOrderBySentDesc(Long toUser, Long fromUser, Pageable pageable);
	List<Message> findByToUserIdAndFromUserIdOrderBySentAsc(Long toUser, Long fromUser, Pageable pageable);
}
