package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TemplateController{

	public static void main(String[] args) {
		//Application.launch(TemplateController.class, args);
	}

	//@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/templates/book-list.fxml"));

		stage.setTitle("FXML Welcome");
		stage.setScene(new Scene(root, 300, 275));
		stage.show();
	}
}