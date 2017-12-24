package com.caveofprogramming.model.dto;

import java.util.Date;

import com.caveofprogramming.model.entity.Message;

public class SimpleMessage {
	private String from;
	private String text;
	private Date date;
	private Long fromUserId;
	private Boolean isReply;

	public SimpleMessage() {

	}

	public SimpleMessage(String text) {
		this.text = text;
	}
	
	public SimpleMessage(Message message, Boolean isReply) {
		
		String firstname = message.getFromUser().getFirstname();
		String surname = message.getFromUser().getSurname();
		String name = firstname + " " + surname;
		
		this.isReply = isReply;
		date = message.getSent();
		from = name;
		fromUserId = message.getFromUser().getId();
		text = message.getText();
	}
	
	public SimpleMessage(Date date, Long fromUserId, String from, String text) {
		this.text = text;
		this.from = from;
		this.date = date;
		this.fromUserId = fromUserId;
	}
	
	public Boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(Boolean isReply) {
		this.isReply = isReply;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	

	public Long getFromUserId() {
		return fromUserId;
	}

	@Override
	public String toString() {
		return "Message [text=" + text + "]";
	}

}
