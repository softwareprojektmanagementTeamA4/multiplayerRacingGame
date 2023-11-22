import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;

public class javaFX extends Application {
    private Socket socket;
    private Label ausgabeGUI;
    @Override
    public void start(Stage primaryStage) {
        ausgabeGUI = new Label();
        ausgabeGUI.setText("Verbinden sie sich mit dem Server!");
        Button btn = new Button();
        btn.setText("Hier Drücken!");

        // Event-Handler für den Button
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                establishConnection();
            }
        });

        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(ausgabeGUI);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Socket.IO Verbindung");
        primaryStage.setScene(scene);
    
        primaryStage.show();
    
    }

    private void establishConnection() {
        try {
            socket = IO.socket("http://serveradresse:port");
    
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Platform.runLater(() -> ausgabeGUI.setText("Verbunden mit dem Server"));
                }
            });
    
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Platform.runLater(() -> ausgabeGUI.setText("Verbindungsfehler: " + args[0]));
                }
            });
    
            socket.connect();
        } catch (URISyntaxException e) {
            // Fehler beim Parsen der URI
            e.printStackTrace();
            Platform.runLater(() -> ausgabeGUI.setText("Ungültige Serveradresse oder Port"));
        } catch (Exception e) {
            // Allgemeiner Fehler
            e.printStackTrace();
            Platform.runLater(() -> ausgabeGUI.setText("Verbindungsfehler: " + e.getMessage()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
