<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-12">
	
		<%--
		<div class="chat-message-keep-logged-in">
			<label for="chat-message-keep-logged-in-checkbox">Keep me logged into this chat; I'm not on a public computer: </label>
			
			<input
				id="chat-message-keep-logged-in-checkbox" type="checkbox" />
		</div>
		--%>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">


			<div class="panel-heading">
				<div class="panel-title">Chat with <c:out value="${chattingWithName}" /></div>
			</div>

			<div class="panel-body">

				<div id="chat-message-view">

					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />

					<div id="chat-message-previous">
						<a id="chat-older-messages" href="#">view older messages</a>
					</div>
					

					<div id="chat-message-record"></div>

					<div id="chat-message-send" class="input-group input-group-lg">

						<textarea class="form-control" id="chat-message-text"
							placeHolder="Enter message"></textarea>
						<span class="input-group-btn">
							<button id="chat-send-button" class="btn btn-primary"
								type="button">Send</button>
						</span>
					</div>
				</div>

			</div>

		</div>

	</div>
</div>


