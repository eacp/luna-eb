package com.eduardocp.lunaexambuilder.view_controller;

import com.eduardocp.lunaexambuilder.MainApp;
import com.eduardocp.lunaexambuilder.apputils.Data;
import com.eduardocp.lunaexambuilder.model.Exam;
import com.eduardocp.lunaexambuilder.model.Question;
import com.eduardocp.utils.Dialogs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Eduardo on 08/06/2017.
 */
public class GenerateExamsCtrl implements Initializable{
	@FXML private TreeView<Question> questionTreeView;
	@FXML private String directory;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(MainApp.currentExam);
		titleField.setText(MainApp.currentExam.title);
		authorField.setText(MainApp.currentExam.author);
	}

	//BEGIN LEFT SIDE---------------------------------------------------------------------------------------------------
	@FXML private TextField titleField,authorField,numberField;
	@FXML private Label directoryLabel;
	//END LEFT SIDE-----------------------------------------------------------------------------------------------------

	//BEGIN TOOLBAR-----------------------------------------------------------------------------------------------------
	@FXML private void goToOverview() throws IOException {
		//Get the current width and height to make the transition consistent
		double w = MainApp.window.getScene().getWidth(),h = MainApp.window.getScene().getHeight();
		//load the FXML
		Parent root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		//Set the scene in the window
		MainApp.window.setScene(new Scene(root,w,h));
	}

	@FXML private void export(){
		try {
			//Create temporal exam to avoid altering the original
			Exam ex = new Exam(titleField.getText(),authorField.getText());
			for (Question q: MainApp.questionObservableList) {ex.questions.add(q);}
			int n = Integer.parseInt(numberField.getText());
			System.out.println(ex);
			System.out.println("-----------------------------------------------");
			for (int i = 0; i < n; i++) {
				ex.shuffle();
				System.out.println(ex);
			}
		}catch (Exception e){
			Dialogs.error("ERROR","Information not valid","The number of versions must be a valid number");
		}
	}

	@FXML
	private void chooseDir(){
		//select a directory
		String dir = Data.getDirectory(MainApp.window);
		//print that directory to the console
		System.out.println(dir);
		//set the directory if not empty
		if (dir != ""){
			directory = dir;directoryLabel.setText(directory);
		}
	}

	//END TOOLBAR-------------------------------------------------------------------------------------------------------
}
