package control;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.dataaccess.DataAccess;
import model.dataaccess.DataAccessFacade;
import model.domain.Book;
import model.domain.BookCopy;
import model.domain.CheckoutRecordEntry;
import model.domain.LibraryMember;
import model.domain.User;
import util.Util;
import view.MainWindow;

public class CheckoutController extends Application {
	private User user;
	private Stage primaryStage;

//	@FXML
//	TextField memberId;
//	@FXML
//	TextField txtIsbn;

	@Override
	public void start(Stage stage) throws Exception {
		this.primaryStage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/view/templates/checkoutbook.fxml"));

		stage.setTitle("FXML Welcome");
		stage.setScene(new Scene(root, 300, 200));

		 TextField memberId = (TextField) root.lookup("#memberId");
		 TextField txtIsbn = (TextField) root.lookup("#txtIsbn");

		Button btnBack = (Button) root.lookup("#btnBack");
		btnBack.setOnAction((event) -> {
			MainWindow mainWindow = new MainWindow(user);
			try {
				mainWindow.start(this.primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Button btnCheckout = (Button) root.lookup("#btnCheckout");
		btnCheckout.setOnAction((event) -> {
			boolean badmember = true;
			boolean badisbn = true;
			DataAccess db = new DataAccessFacade();
			HashMap<String, LibraryMember> list_members = db.readMemberMap();
			HashMap<String, Book> books = db.readBooksMap();
			for (Entry<String, LibraryMember> entry : list_members.entrySet()) {
				if (entry.getValue().getMemberId().equals(memberId.getText())) {
					badmember = false;
					break;

				}
			}

			if (badmember) {
				Util.showAlert("Member Id No found", "Not data found", AlertType.WARNING);
				return;
			}

			Book temp_book = new Book();
			for (Entry<String, Book> entry : books.entrySet()) {
				if (entry.getValue().getIsbn().equals(txtIsbn.getText())) {
					badisbn = false;
					temp_book = entry.getValue();

				}
			}

			if (badisbn) {
				Util.showAlert("Book Isbn No found", "Not data found", AlertType.WARNING);
				return;
			} else {
				if (!temp_book.isAvailable()) {
					Util.showAlert("Book is avaible in this moment ", "Not data found", AlertType.INFORMATION);
					CheckoutRecordEntry cre = checkoutABook(memberId.getText(), txtIsbn.getText());
					return;
				}
			}

			// Util.showAlert("All is good", "All is good", AlertType.INFORMATION);

		});

		stage.show();
	}

	protected static CheckoutRecordEntry checkoutABook(String userId, String isbn) {
		DataAccess db = new DataAccessFacade();
		HashMap<String, LibraryMember> memMap = db.readMemberMap();
		LibraryMember mb = memMap.get(userId);
		if (mb == null) {
			Util.showAlert("Member does not exist", "Not data found", AlertType.ERROR);
		}

		HashMap<String, Book> bookMap = db.readBooksMap();
		Book bk = bookMap.get(isbn);
		if (bk == null || !bk.isAvailable()) {
			Util.showAlert("Book does not exist", "Not data found", AlertType.ERROR);
		}

		else {
			BookCopy bc = bk.getNextAvailableCopy();

			CheckoutRecordEntry cre = mb.addCheckoutRecordEntry(bc, LocalDate.now(),
					LocalDate.now().plusDays(bk.getMaxCheckoutLength()));
			bc.changeAvailability();
			db.saveNewMember(mb);
			db.saveAbook(bk);
			return cre;
		}
		return null;
	}

	public static void main(String[] args) {
		Application.launch(CheckoutController.class, args);
	}
}