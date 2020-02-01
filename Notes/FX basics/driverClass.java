
import java.util.Iterator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class driverClass extends Application{
 
	TextField text;
	Button btn;
	Button btn2;
	GenericQueue<String> q;

public static void main(String[] args) {

launch(args);
}
 

public void start(Stage primaryStage) throws Exception {
	primaryStage.setTitle("Our first JavaFX Experience!");
	
	text = new TextField();
	btn = new Button("Press Me");
	btn2 = new Button("print list");
	
	btn2.setOnAction(new EventHandler<ActionEvent>(){
		
		public void handle(ActionEvent event){
			
			Iterator i = q.createIterator();
			
			while(i.hasNext()){
				System.out.println(i.next());
			}
		}
	});
	
	q = new GenericQueue<String>("Start list: ");
	
	btn.setOnAction(new EventHandler<ActionEvent>(){
		
		public void handle(ActionEvent event){
			System.out.println(text.getText());
			q.enqueue(text.getText());
			text.clear();
		}
	});
	
	BorderPane pane = new BorderPane();
	pane.setPadding(new Insets(70));
	
	VBox paneCenter = new VBox();
	paneCenter.setSpacing(10);
	pane.setCenter(paneCenter);
	pane.setLeft(btn2);
	
	paneCenter.getChildren().add(text);
	paneCenter.getChildren().add(btn);
	
	Scene scene = new Scene(pane, 400, 200);
	primaryStage.setScene(scene);
	primaryStage.show();
	
	}

}

