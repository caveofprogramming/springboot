package com.caveofprogramming.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.dto.SimpleMessage;
import com.caveofprogramming.model.entity.Message;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.model.repository.MessageDao;

@Service
public class MessageService {

	@Value("${messages.inbox.pagesize}")
	private int pageSize;
	
	@Value("${messages.chatview.pagesize}")
	private int maxChatMessages;

	@Autowired
	private MessageDao messageDao;

	public void save(Message message) {
		messageDao.save(message);
	}

	public void save(SiteUser toUser, SiteUser fromUser, String text) {
		messageDao.save(new Message(toUser, fromUser, text));
	}

	public List<SimpleMessage> getChat(Long toUserId, Long fromUserId, int pageNumber) {

		PageRequest request = new PageRequest(pageNumber - 1, maxChatMessages);
		List<Message> sentTo = messageDao.findByToUserIdAndFromUserIdOrderBySentDesc(toUserId, fromUserId, request);
		List<Message> receivedFrom = messageDao.findByToUserIdAndFromUserIdOrderBySentDesc(fromUserId, toUserId,
				request);

		List<Message> messages = new ArrayList<Message>(sentTo.size() + receivedFrom.size());

		messages.addAll(sentTo);
		messages.addAll(receivedFrom);

		List<SimpleMessage> chatMessages = messages.stream()
				.map(p -> new SimpleMessage(p, p.getFromUser().getId().compareTo(fromUserId) == 0))
				.sorted(Comparator.comparing(m -> m.getDate())).collect(Collectors.toList());

		return chatMessages;
	}

	public Page<SimpleMessage> getMessages(Long toUserId, int pageNumber) {
		PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		Page<Message> results = messageDao.findByToUserIdOrderBySentDesc(toUserId, request);

		Converter<Message, SimpleMessage> converter = new Converter<Message, SimpleMessage>() {
			public SimpleMessage convert(Message message) {
				return new SimpleMessage(message, true);
			}

		};

		return results.map(converter);
	}
}
