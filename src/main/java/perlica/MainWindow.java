package perlica;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private VBox dialogContainer;
	@FXML
	private TextField userInput;
	@FXML
	private Button sendButton;

	private Perlica Perlica;

	private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
	private Image PerlicaImage = new Image(this.getClass().getResourceAsStream("/images/perlica.png"));

	@FXML
	public void initialize() {
		scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
	}

	/** Injects the Perlica instance */
	public void setPerlica(Perlica d) {
		Perlica = d;
	}

	/**
	 * Creates two dialog boxes, one echoing user input and the other containing Perlica's reply and then appends them to
	 * the dialog container. Clears the user input after processing.
	 */
	@FXML
	private void handleUserInput() {
		String input = userInput.getText();
		String response = Perlica.getResponse(input);
		dialogContainer.getChildren().addAll(
				DialogBox.getUserDialog(input, userImage),
				DialogBox.getPerlicaDialog(response, PerlicaImage)
		);
		userInput.clear();

		if (input.trim().equals("bye")) {
			Platform.exit();
			System.exit(0);
		}
	}
}
