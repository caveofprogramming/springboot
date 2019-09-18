package com.caveofprogramming.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.entity.Message;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.model.repository.MessageDao;

@Service
public class MessageService {
	
	@Autowired
	private MessageDao messageDao;
	
	@Async
	public void save(SiteUser fromUser, SiteUser toUser, String text) {
		messageDao.save(new Message(fromUser, toUser, text));
	}
}
