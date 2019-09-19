package com.caveofprogramming.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.dto.SimpleMessage;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.service.MessageService;
import com.caveofprogramming.service.UserService;

@Controller
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate simpleMessagingTemplate;
	
	@Autowired
	private Util util;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value="/conversation/{otherUserId}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	List<SimpleMessage> fetchConversation(@PathVariable("otherUserId") Long otherUserId) {
		
		SiteUser thisUser = util.getUser();
		
		List<SimpleMessage> list = messageService.fetchConversation(thisUser.getId(), otherUserId, 0);
		
		return list;
	}

	@RequestMapping("/chatview/{chatWithUserID}")
	ModelAndView chatView(ModelAndView modelAndView, @PathVariable Long chatWithUserID) {
		
		SiteUser thisUser = util.getUser();
		String chatWithUserName = userService.getUserName(chatWithUserID);
		
		modelAndView.setViewName("chat.chatview");
		modelAndView.getModel().put("thisUserID", thisUser.getId());
		modelAndView.getModel().put("chatWithUserID", chatWithUserID);
		modelAndView.getModel().put("chatWithUserName", chatWithUserName);
		
		return modelAndView;
	}
	
	
	@MessageMapping("/message/send/{toUserId}")
	public void send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserId) {
		System.out.println(message);
		
		// Get details for sending user (current user)
		String fromUsername = principal.getName();
		SiteUser fromUser = userService.get(fromUsername);
		Long fromUserId = fromUser.getId();
		
		// Get details for user we are chatting with.
		Optional<SiteUser> toUserOpt = userService.get(toUserId);
		SiteUser toUser = toUserOpt.get();
		String toUsername = toUser.getEmail();
		
		String returnReceiptQueue = String.format("/queue/%d", toUserId);
		String toUserQueue = String.format("/queue/%d", fromUserId);
		
		messageService.save(fromUser, toUser, message.getText());
		
		message.setIsReply(false);
		simpleMessagingTemplate.convertAndSendToUser(fromUsername, returnReceiptQueue, message);
		
		message.setIsReply(true);
		simpleMessagingTemplate.convertAndSendToUser(toUsername, toUserQueue, message);
	}
}












