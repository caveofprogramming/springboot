package com.caveofprogramming.controllers;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

	@RequestMapping("/messages")
	ModelAndView messages(ModelAndView modelAndView, @RequestParam("p") int pageNumber) {

		SiteUser thisUser = util.getUser();
		Page<SimpleMessage> messages = messageService.getMessages(thisUser.getId(), pageNumber);
		modelAndView.getModel().put("messageList", messages);
		modelAndView.setViewName("app.messages");
		return modelAndView;
	}

	@RequestMapping("/getchat/{withUserId}")
	@ResponseBody
	List<SimpleMessage> getchat(ModelAndView modelAndView, @PathVariable Long withUserId) {
		SiteUser thisUser = util.getUser();

		List<SimpleMessage> messages = messageService.getChat(thisUser.getId(), withUserId, 1);
		
		return messages;
	}

	@RequestMapping("/chatview/{sendToId}")
	ModelAndView invalidUser(ModelAndView modelAndView, @PathVariable Long sendToId) {
		SiteUser thisUser = util.getUser();
		
		String chattingWithName = userService.getUserName(sendToId);

		modelAndView.getModel().put("chattingWithName", chattingWithName);
		modelAndView.getModel().put("thisUserId", thisUser.getId());
		modelAndView.getModel().put("toUserId", sendToId);
		modelAndView.setViewName("app.chatview");
		return modelAndView;
	}

	@MessageMapping("/message/send/{toUserId}")
	public SimpleMessage send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserId)
			throws Exception {
		
		logger.debug("Sending message to user: " + toUserId);

		String fromUsername = principal.getName();
		SiteUser sentFrom = userService.get(fromUsername);

		String replyQueue = String.format("/queue/%d", sentFrom.getId());

		SiteUser sendTo = userService.get(toUserId);
		String sendToUsername = sendTo.getEmail();

		messageService.save(sendTo, sentFrom, message.getText());

		simpleMessagingTemplate.convertAndSendToUser(sendToUsername, replyQueue, message);
		return message;
	}

}
