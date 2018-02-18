package com.caveofprogramming.controllers;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.dto.ChatPageRequest;
import com.caveofprogramming.model.dto.SimpleMessage;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.service.MessageService;
import com.caveofprogramming.service.UserService;

@Controller
public class MessageController {

	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessagingTemplate simpleMessagingTemplate;

	@Autowired
	private Util util;

	@Autowired
	private MessageService messageService;

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	ModelAndView messages(ModelAndView modelAndView, @RequestParam("p") int pageNumber) {

		SiteUser thisUser = util.getUser();
		Page<SimpleMessage> messages = messageService.getMessages(thisUser.getId(), pageNumber);
		modelAndView.getModel().put("messageList", messages);
		modelAndView.getModel().put("pageNumber", pageNumber);
		modelAndView.getModel().put("userId", thisUser.getId());

		modelAndView.setViewName("app.messages");
		return modelAndView;
	}

	@RequestMapping(value = "/messages", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	List<SimpleMessage> fetchMessageList(@RequestBody ChatPageRequest chatPageRequest) {

		SiteUser thisUser = util.getUser();
		return messageService.fetchMessageList(thisUser.getId(), chatPageRequest.getPage());
	}

	@RequestMapping(value = "/getchat", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	List<SimpleMessage> getchat(@RequestBody ChatPageRequest chatPageRequest) {

		SiteUser thisUser = util.getUser();

		List<SimpleMessage> messages = messageService.fetchConversation(thisUser.getId(),
				chatPageRequest.getChatWithUserID(), chatPageRequest.getPage());

		return messages;
	}

	@RequestMapping("/chatview/{chatWithUserID}")
	ModelAndView chatView(ModelAndView modelAndView, @PathVariable Long chatWithUserID) {
		SiteUser thisUser = util.getUser();

		String chattingWithName = userService.getUserName(chatWithUserID);

		modelAndView.getModel().put("chattingWithName", chattingWithName);
		modelAndView.getModel().put("thisUserId", thisUser.getId());
		modelAndView.getModel().put("chatWithUserID", chatWithUserID);
		modelAndView.setViewName("app.chatview");
		return modelAndView;
	}

	@MessageMapping("/message/send/{toUserId}")
	public SimpleMessage send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserId)
			throws Exception {

		String fromUsername = principal.getName();
		SiteUser sentFrom = userService.get(fromUsername);

		SiteUser sendTo = userService.get(toUserId);
		String sendToUsername = sendTo.getEmail();

		String replyQueue = String.format("/queue/%d", sentFrom.getId());
		String returnQueue = String.format("/queue/%d", sendTo.getId());

		messageService.save(sendTo, sentFrom, message.getText());

		message.setFromUserId(sentFrom.getId());
		message.setSent(new Date());

		logger.debug("Sending message to user: " + toUserId);
		logger.debug(message.toString());

		message.setFrom(sentFrom.getFirstname() + " " + sentFrom.getSurname());
		message.setSent(new Date());
		message.setFromUserId(sentFrom.getId());

		// Send the message to the recipient.
		message.setIsReply(true);
		simpleMessagingTemplate.convertAndSendToUser(sendToUsername, replyQueue, message);

		// Also send the message back to the user who sent it, so it appears
		// after they
		// type it. If it doesn't appear, they'll know it has failed to send.
		message.setIsReply(false);
		simpleMessagingTemplate.convertAndSendToUser(fromUsername, returnQueue, message);

		return message;
	}

}
