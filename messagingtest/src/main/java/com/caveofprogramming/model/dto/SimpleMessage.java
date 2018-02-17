package com.caveofprogramming.model.dto;

import java.util.Date;

import com.caveofprogramming.model.entity.Message;

public class SimpleMessage {
	private String from;
	private String text;
	private Date sent;
	private Long fromUserId;
	private Boolean isReply;

	public SimpleMessage() {

	}

	public SimpleMessage(String text) {
		this.text = text;
		this.sent = new Date();
	}
	
	public SimpleMessage(Message message, Boolean isReply) {
		
		String firstname = message.getFromUser().getFirstname();
		String surname = message.getFromUser().getSurname();
		String name = firstname + " " + surname;
		
		this.isReply = isReply;
		sent = message.getSent();
		from = name;
		fromUserId = message.getFromUser().getId();
		text = message.getText();
	}
	
	public SimpleMessage(Date sent, Long fromUserId, String from, String text) {
		this.text = text;
		this.from = from;
		this.sent = sent;
		this.fromUserId = fromUserId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(Boolean isReply) {
		this.isReply = isReply;
	}
	

	@Override
	public String toString() {
		return "SimpleMessage [from=" + from + ", text=" + text + ", sent=" + sent + ", fromUserId=" + fromUserId
				+ ", isReply=" + isReply + "]";
	}
	
	
	
}
