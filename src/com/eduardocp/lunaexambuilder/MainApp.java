package com.eduardocp.lunaexambuilder;

import com.eduardocp.lunaexambuilder.model.Exam;
import com.eduardocp.lunaexambuilder.model.Question;
import com.eduardocp.lunaexambuilder.view_controller.EditQuestionCtrl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
	//MYSTIC CONSTANTS
    private static final int MIN_WIDTH = 400,MIN_HEIGHT = 250;
    public static final String APP_TITLE = "Luna Exam Builder";
	//GLOBAL VARIABLES FOR EASY ACCESS. Sometimes IDK what I'm doing
	public static Exam currentExam = new Exam("New document","Someone");
	public static ObservableList<Question> questionObservableList = FXCollections.observableArrayList();
	public static String currentFilePath = "";
	public static int currentEditingQuestionIndex;
	//make stage accessible everywhere in the application
	public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
    	window = primaryStage;
    	//Create the normal window
        Parent root = FXMLLoader.load(getClass().getResource("view_controller/Overview.fxml"));
        primaryStage.setTitle("New Document - Luna Exam Builder");
        primaryStage.setScene(new Scene(root, 1000, 625));
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        //Set the icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/icon.png")));
        //Show
        primaryStage.show();
    }


    public static void main(String[] args) {launch(args);}

    public static boolean showEditQuestionDialog(Stage ps,Question question,String title){
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view_controller/EditQuestion.fxml"));
			GridPane page = loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(title);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("resources/icon.png")));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(ps);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.setWidth(450);
			dialogStage.setHeight(700);
			dialogStage.setResizable(false);

			// Set the question into the controller.
			EditQuestionCtrl controller = loader.getController();
			controller.dialogStage = dialogStage;
			controller.setQuestion(question);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.okClicked;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
