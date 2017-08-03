package assignment7;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientMain extends Application{
			
	static String message;
	
	public static void main(String[] args){
		new Thread(() -> launch(args)).start();
		new Thread(() -> new Client()).start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane gp = new GridPane();
		TextField t = new TextField("Type something");
		
		Button send = new Button("Send");
		send.setOnAction((ActionEvent event) -> {
			message = t.getText();
		});
		//send.setOnAction((ActionEvent event) -> System.out.println(t.getText()));

		gp.add(t, 0, 0);
		gp.add(send, 1, 0);
		
		primaryStage.setTitle("Client");
		primaryStage.setScene(new Scene(gp, 350, 400));
		primaryStage.show();
	}
}
