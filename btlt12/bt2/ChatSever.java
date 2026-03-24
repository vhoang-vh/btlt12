import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {

    private static Set<WebSocket> clients = new HashSet<>();

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Client kết nối: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Client ngắt kết nối");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Nhận: " + message);

        // gửi cho tất cả client
        for (WebSocket client : clients) {
            client.send(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server chạy tại port 8080");
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(8080);
        server.start();
    }
}