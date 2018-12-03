package com.caveofprogramming.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.dto.SimpleMessage;
import com.caveofprogramming.model.entity.Message;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.model.repository.MessageDao;

@Service
public class MessageService {

	@Value("${messages.inbox.pagesize}")
	private int pageSize;

	@Value("${messages.chat.pagesize}")
	private int messagesPerPage;

	@Autowired
	private MessageDao messageDao;

	public void save(Message message) {
		messageDao.save(message);
	}

	@Async
	public void save(SiteUser toUser, SiteUser fromUser, String text) {
		messageDao.save(new Message(toUser, fromUser, text));
	}

	/*
	 * Get the conversation between two particular users.
	 */

	public List<SimpleMessage> fetchConversation(Long toUserId, Long fromUserId, int page) {
		
		PageRequest request = new PageRequest(page, messagesPerPage);
		
		Slice<Message> conversation = messageDao.fetchConversation(toUserId, fromUserId, request);

		// Mark any received messages here as read, although ideally we should
		// wait until we're certain they've been displayed. 
		// We can figure out which are replies via the user IDs.
		conversation.forEach(m -> {
			if (m.getFromUser().getId().compareTo(fromUserId) == 0) {
				m.setRead(true);
				messageDao.save(m);
			}
		});
		
		// Map messages objects containing full user details to message object
		// intended
		// for display purposes. Compare each message's 'fromUser' ID with the
		// given 'fromUserId'
		// parameter to figure out whether it counts as a reply or a sent
		// message.
		List<SimpleMessage> messages = conversation.map(m -> new SimpleMessage(m, m.getFromUser().getId().compareTo(fromUserId) == 0))
				.getContent();
		
		messages.forEach(m -> { System.out.println("LOADED MESSAGE " + m);});
		
		return messages;
	}

	/*
	 * Get the list of all messages sent to the user.
	 */

	/*
	public Page<SimpleMessage> getMessages(Long toUserId, int pageNumber) {
		PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		Page<Message> results = messageDao.findByToUserIdAndReadFalseOrderBySentAsc(toUserId, request);

		Converter<Message, SimpleMessage> converter = new Converter<Message, SimpleMessage>() {
			public SimpleMessage convert(Message message) {
				return new SimpleMessage(message, true);
			}

		};

		return results.map(converter);
	}
	*/
	
	/*
	 * Get the list of all messages sent to the user in page form.
	 */
	public Page<SimpleMessage> fetchMessageListPage(Long toUserId, int pageNumber) {
		
		PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		Page<Message> results = messageDao.findByToUserIdAndReadFalseOrderBySentDesc(toUserId, request);

		return results.map(m -> new SimpleMessage(m, true));
	}
	
	/*
	 * Get the list of all messages sent to the user as a list.
	 */
	public List<SimpleMessage> fetchMessageList(Long toUserId, int pageNumber) {
		
		PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		Page<Message> results = messageDao.findByToUserIdAndReadFalseOrderBySentDesc(toUserId, request);

		return results.map(m -> new SimpleMessage(m, true)).getContent();
	}

	public Long fetchMessageCount(Long userId) {
		
		return messageDao.fetchUnreadMessageCount(userId);
	}
}
