package com.caveofprogramming.model.dto;

public class ChatPageRequest {

	private int page;
	private Long chatWithUserID;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Long getChatWithUserID() {
		return chatWithUserID;
	}

	public void setChatWithUserID(Long chatWithUserID) {
		this.chatWithUserID = chatWithUserID;
	}

}
