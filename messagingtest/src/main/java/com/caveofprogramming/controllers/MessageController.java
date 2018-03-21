package com.caveofprogramming.controllers;

import java.security.Principal;
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

	/*
	 * Display the actual page where we'll output the list of messages uses AJAX.
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	ModelAndView messages(ModelAndView modelAndView, @RequestParam("p") int pageNumber) {

		SiteUser thisUser = util.getUser();
		Page<SimpleMessage> messages = messageService.fetchMessageListPage(thisUser.getId(), pageNumber);
		modelAndView.getModel().put("messageList", messages);
		modelAndView.getModel().put("pageNumber", pageNumber);
		modelAndView.getModel().put("userId", thisUser.getId());

		modelAndView.setViewName("app.messages");
		return modelAndView;
	}

	/*
	 * This is for AJAX to fetch the list of messages.
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	List<SimpleMessage> fetchMessageList(@RequestBody ChatPageRequest chatPageRequest) {

		SiteUser thisUser = util.getUser();
		return messageService.fetchMessageList(thisUser.getId(), chatPageRequest.getPage());
	}

	@RequestMapping(value = "/conversation/{otherUserId}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	List<SimpleMessage> fetchConversation(@PathVariable("otherUserId") Long otherUserId,
			@RequestBody ChatPageRequest chatPageRequest) {

		SiteUser thisUser = util.getUser();

		List<SimpleMessage> messages = messageService.fetchConversation(thisUser.getId(), otherUserId,
				chatPageRequest.getPage());

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
	public void send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserId)
			throws Exception {

		// Get the details of the user that's sending the message.
		String fromUsername = principal.getName();
		SiteUser sentFrom = userService.get(fromUsername);
		Long fromUserId = sentFrom.getId();
		String sentFromFullName = sentFrom.getFirstname() + " " + sentFrom.getSurname();

		// Get the details of the user we're sending the message to.
		SiteUser sendTo = userService.get(toUserId);
		String sendToUsername = sendTo.getEmail();

		String sendToQueue = String.format("/queue/%d", toUserId);
		String returnReceiptQueue = String.format("/queue/%d", fromUserId);
		String messageAlertQueue = "/queue/newmessage";

		// Save the message to it can be retrieved in future.
		// This is done asynchronously, so it doesn't hold up
		// message transmission.
		messageService.save(sendTo, sentFrom, message.getText());

		// Fill in details so we can send the message immediately
		// on its way without involving the database.
		message.setFromUserId(fromUserId);
		message.setFrom(sentFromFullName);
		message.setSent(new Date());
		message.setSent(new Date());

		// Send the message to the recipient.
		message.setIsReply(true);
		logger.debug("Sending message to user on " + sendToQueue);
		logger.debug(message.toString());
		simpleMessagingTemplate.convertAndSendToUser(sendToUsername, sendToQueue, message);

		// Also send the message back to the user who sent it, so it appears
		// after they type it. If it doesn't appear, they'll know it has
		// failed to send.
		message.setIsReply(false);
		logger.debug("Sending message back to current user on " + returnReceiptQueue);
		logger.debug(message.toString());
		simpleMessagingTemplate.convertAndSendToUser(fromUsername, returnReceiptQueue, message);
		
		// Finally send a message alerting the destination user that they have received
		// a new message from someone.
		logger.debug("Sending message alert notification to current user on " + messageAlertQueue);
		logger.debug(message.toString());
		simpleMessagingTemplate.convertAndSendToUser(fromUsername, messageAlertQueue, message);
	}

}
