package com.caveofprogramming.model.dto;

public class ChatPageRequest {

	private int page;
	private Long toUserId;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long withUserId) {
		this.toUserId = withUserId;
	}

}
