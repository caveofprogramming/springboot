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
	private Date sent;

	public Message() {

	}

	@PrePersist
	protected void onCreate() {
		if (sent == null) {
			sent = new Date();
		}
	}

	public Message(SiteUser fromUser, SiteUser toUser, String text) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.text = text;
		this.read = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SiteUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(SiteUser fromUser) {
		this.fromUser = fromUser;
	}

	public SiteUser getToUser() {
		return toUser;
	}

	public void setToUser(SiteUser toUser) {
		this.toUser = toUser;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

}
