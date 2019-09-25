package com.caveofprogramming.model.dto;

import java.util.Date;

import com.caveofprogramming.model.entity.Message;

public class SimpleMessage {
	private Long id;
	private String from;
	private String text;
	private Date sent;
	private Long fromUserId;
	private Boolean isReply;

	
	public SimpleMessage(String text) {
		this.text = text;
		this.sent = new Date();
	}
	
	public SimpleMessage() {
		
	}
	
	public SimpleMessage(Message m, Boolean isReply) {
		this.from = m.getFromUser().getFirstname() + " " + m.getFromUser().getSurname();
		this.text = m.getText();
		this.sent = m.getSent();
		this.fromUserId = m.getFromUser().getId();
		this.isReply = isReply;
		this.id = m.getId();
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
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
