import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatClient extends Application {

    private TextArea chatArea;
    private TextField inputField;
    private WebSocketClient client;

    @Override
    public void start(Stage stage) throws Exception {

        chatArea = new TextArea();
        chatArea.setEditable(false);

        inputField = new TextField();
        Button sendBtn = new Button("Gửi");

        VBox root = new VBox(10, chatArea, inputField, sendBtn);

        connectWebSocket();

        sendBtn.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Chat Client");
        stage.show();
    }

    private void connectWebSocket() throws Exception {
        client = new WebSocketClient(new URI("ws://localhost:8080")) {

            @Override
            public void onOpen(ServerHandshake handshake) {
                appendMessage("Đã kết nối server");
            }

            @Override
            public void onMessage(String message) {
                Platform.runLater(() -> appendMessage(message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                appendMessage("Mất kết nối");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        client.connect();
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (!msg.isEmpty()) {
            client.send(msg);
            inputField.clear();
        }
    }

    private void appendMessage(String msg) {
        chatArea.appendText(msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}