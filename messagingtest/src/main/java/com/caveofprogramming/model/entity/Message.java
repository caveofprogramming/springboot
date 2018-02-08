package com.caveofprogramming.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "from_user", nullable = false)
	private SiteUser fromUser;

	@ManyToOne
	@JoinColumn(name = "to_user", nullable = false)
	private SiteUser toUser;

	@Column(name = "message_text", length = 1024, nullable = false)
	String text;
	
	@Column(name = "is_read", nullable = false)
	Boolean read;

	@Column(name = "sent")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Date sent;

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	@PrePersist
	protected void onCreate() {
		if (sent == null) {
			sent = new Date();
		}
	}
	
	public Message() {
		
	}

	public Message(SiteUser toUser, SiteUser fromUser, String text) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.text = text;
		read = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

	public SiteUser getToUser() {
		return toUser;
	}

	public void setToUser(SiteUser toUser) {
		this.toUser = toUser;
	}

	public SiteUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(SiteUser fromUser) {
		this.fromUser = fromUser;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	@Override
	public String toString() {
		return "Message [fromUser=" + fromUser + ", toUser=" + toUser + ", text=" + text + "]";
	}

}
