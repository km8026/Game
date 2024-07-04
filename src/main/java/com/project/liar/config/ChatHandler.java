package com.project.liar.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
// 1 // 3333
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
// 2
@Component
public class ChatHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();
    private Map<String, String> sessionRoomMap = new ConcurrentHashMap<>(); // 세션과 방 ID를 매핑
    private Map<String, List<WebSocketSession>> roomSessionsMap = new ConcurrentHashMap<>(); // 방 ID와 세션 리스트를 매핑

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 텍스트 메시지를 수신했을 때 호출되는 메서드
        System.out.println("handleTextMessage"); // 메시지 수신을 알리는 로그 출력
        System.out.println(message.getPayload()); // 수신한 메시지의 내용 출력
        String msg = message.getPayload(); // 메시지 내용을 변수에 저장

        if (msg.startsWith("create:::")) {
            // 메시지가 "create:::"로 시작하면 방 생성과 관련된 처리
            String roomId = msg.split(":::")[1];
            sessionRoomMap.put(session.getId(), roomId);

            List<WebSocketSession> roomSessions = roomSessionsMap.getOrDefault(roomId, new ArrayList<>());
            roomSessions.add(session);
            roomSessionsMap.put(roomId, roomSessions);
        }

        // 모든 세션에 대해 수신한 메시지를 전송
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 웹소켓 연결이 닫혔을 때 호출되는 메서드
        sessions.remove(session); // 세션 리스트에서 해당 세션을 제거
        String roomId = sessionRoomMap.remove(session.getId());
        if (roomId != null) {
            List<WebSocketSession> roomSessions = roomSessionsMap.get(roomId);
            if (roomSessions != null) {
                roomSessions.remove(session);
                if (roomSessions.isEmpty()) {
                    roomSessionsMap.remove(roomId);
                }
            }
        }
    }

    public void broadcastMessage(String message) throws Exception {
        // 서버에서 모든 클라이언트로 메시지를 전송할 때 호출되는 메서드
        System.out.println("broadcastMessage"); // 브로드캐스트 메시지 전송을 알리는 로그 출력

        // 모든 세션에 대해 전달받은 메시지를 전송
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
