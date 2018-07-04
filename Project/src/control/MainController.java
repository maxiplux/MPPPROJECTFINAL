package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.domain.User;

public class MainController implements Initializable {
	private User user;
	private Stage primaryStage;

	@FXML
	private Button btnCheckoutBooks;
	@FXML
	private Button btnBooks;
	@FXML
	private Button btnLibraryMembers;

	public MainController() {
		super();
	}

	public MainController(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		btnCheckoutBooks.setOnAction((event) -> {
			CheckoutController checkoutcontroller = new CheckoutController();
			try {
				checkoutcontroller.start(this.primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		btnBooks.setOnAction((event) -> {
			BookController bookcontroller = new BookController(this.user);
			try {
				bookcontroller.start(this.primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

		});

		btnLibraryMembers.setOnAction((event) -> {
			System.out.println("btnLibraryMembers");

			LibraryMemberController librarymember = new LibraryMemberController();
			try {
				librarymember.start(this.primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}
}