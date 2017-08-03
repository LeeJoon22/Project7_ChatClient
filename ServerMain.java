package assignment7;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerMain extends Application{
			
	static Text t;
	
	public static void main(String[] args){
		new Thread(() -> launch(args)).start();
		new Thread(() -> new Server()).start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane gp = new GridPane();
		t = new Text();
		
		Button quit = new Button("Quit");
		quit.setOnAction((ActionEvent event) -> System.exit(0));
		quit.setTextFill(javafx.scene.paint.Color.RED);
		
		gp.add(t, 0, 0);
		gp.add(quit, 0, 1);
		
		primaryStage.setTitle("Server");
		primaryStage.setScene(new Scene(gp, 350, 400));
		primaryStage.show();
	}
	
	public static void updateText(String s){
		t.setText(t.getText() + "\n" + s);
		System.out.println("Got there");
	}
}


