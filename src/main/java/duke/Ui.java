package duke;

import java.util.Objects;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class encapsulates the UI of Duke and its interactions with the user.
 *
 * @author Kleon Ang
 */
public class Ui {
    public static final double STAGE_MIN_HEIGHT = 600.0;
    public static final double STAGE_MIN_WIDTH = 400.0;
    public static final int SCROLL_PANE_WIDTH = 385;
    public static final int SCROLL_PANE_HEIGHT = 535;
    public static final double TEXT_FIELD_WIDTH = 325.0;
    public static final double SEND_BUTTON_WIDTH = 55.0;
    private final ScrollPane scrollPane;
    private final VBox dialogContainer;
    private final TextField userInput;
    private final Image user = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/PepeJam.png")));
    private final Image duke = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/SirDuke.jpg")));

    /**
     * Constructor for a Ui class.
     *
     * @param stage The Stage for the user interface.
     */
    public Ui(Stage stage) {
        // Setting up required components
        // The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        Button sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        Scene scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        // Formatting the window to look as expected
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.setMinWidth(STAGE_MIN_WIDTH);

        mainLayout.setPrefSize(STAGE_MIN_WIDTH, STAGE_MIN_HEIGHT);

        scrollPane.setPrefSize(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        userInput.setPrefWidth(TEXT_FIELD_WIDTH);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput , 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        // Greet user
        Label greeting = new Label("Hello, I'm Duke!\nWhat can I do for you?");
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(greeting, new ImageView(duke)));

        // Add functionality to handle user input
        sendButton.setOnMouseClicked((event) -> {
            dialogContainer.getChildren().add(getDialogLabel(userInput.getText()));
            userInput.clear();
        });
        userInput.setOnAction((event) -> {
            dialogContainer.getChildren().add(getDialogLabel(userInput.getText()));
            userInput.clear();
        });

        // Scroll down to the end every time dialogContainer's height changes
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Add functionality to handle user input
        sendButton.setOnMouseClicked((event) -> handleUserInput());
        userInput.setOnAction((event) -> handleUserInput());
    }

    /**
     * Creates a label with the specified text and adds it to the dialog container.
     *
     * @param text String containing text to add
     * @return A label with the specified text that has word wrap enabled.
     */
    private Label getDialogLabel(String text) {
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);
        return textToAdd;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        Label userText = new Label(userInput.getText());
        Label dukeText;
        try {
            dukeText = new Label(Duke.getResponse(userInput.getText()));
        } catch (DukeException e) {
            dukeText = new Label(e.getMessage());
        }
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, new ImageView(user)),
                DialogBox.getDukeDialog(dukeText, new ImageView(duke))
        );
        userInput.clear();
    }

    /**
     * Prints the given string formatted in a reply.
     *
     * @param string The string to format in a reply.
     */
    public void printReply(String string) {
        Label reply = new Label(string);
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(reply, new ImageView(duke)));
    }

    /**
     * Prints an error message formatted in a reply for an input file name that does not exist.
     *
     * @param fileName The name of the file that does not exist.
     */
    public void showLoadingError(String fileName) {
        printReply(fileName + " not found. File has been created.");
    }

    /**
     * Prints a success message formatted in a reply for an input file name that does not exist.
     *
     * @param fileName The name of the file that does not exist.
     */
    public void showLoadingSuccess(String fileName) {
        printReply("I found a " + fileName + " file! Your tasks have been imported.");
    }
}
