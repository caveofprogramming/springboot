package com.caveofprogramming.tests;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
@Transactional
@Ignore
public class WebSocketTest {

	static final String WEBSOCKET_URI = "/app/message/send/2";
	static final String WEBSOCKET_SUSBSCRIBE = "/user/queue/1";

	BlockingQueue<String> blockingQueue;
	WebSocketStompClient stompClient;

	@Before
	public void setup() {
		blockingQueue = new LinkedBlockingDeque<>();
		stompClient = new WebSocketStompClient(
				new SockJsClient(Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
	}

	@Test
	public void shouldReceiveAMessageFromTheServer() throws Exception {
		StompSession session = stompClient.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
		}).get(1, TimeUnit.SECONDS);
		session.subscribe(WEBSOCKET_SUSBSCRIBE, new DefaultStompFrameHandler());

		String message = "MESSAGE TEST";
		session.send(WEBSOCKET_SUSBSCRIBE, message.getBytes());

		Assert.assertEquals(message, blockingQueue.poll(1, TimeUnit.SECONDS));
	}

	class DefaultStompFrameHandler implements StompFrameHandler {
	

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			blockingQueue.offer(new String((byte[]) o));
		}

		@Override
		public java.lang.reflect.Type getPayloadType(StompHeaders arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
