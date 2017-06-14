package com.eduardocp.lunaexambuilder.view_controller;

import com.eduardocp.lunaexambuilder.MainApp;
import com.eduardocp.lunaexambuilder.utils.*;
import com.eduardocp.lunaexambuilder.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.*;

/**
 * Created by Eduardo on 08/06/2017.
 */
public class GenerateExamsCtrl implements Initializable{
	private static final String[] FORMATS = {"text","html (coming soon)","word (coming soon)","console"};

	//a list to hold the answers to the exams
	private List<String> answers = new ArrayList<>();

	@FXML private TreeView<Question> questionTreeView;
	//this String holds the path to the directory where the
	@FXML private String directory;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//Set fields
		titleField.setText(MainApp.currentExam.title);
		authorField.setText(MainApp.currentExam.author);
		//Set format selection box
		formatBox.setItems(FXCollections.observableArrayList(FORMATS));
		formatBox.getSelectionModel().select(3);
		//set max export label
		System.out.println(MainApp.questionObservableList.size());
		int max = factorial(MainApp.questionObservableList.size());
		maxExportLabel.setText("You can generate up to "+ NumberFormat.getIntegerInstance().format(max) + " different exams");

		//make the tree
		TreeItem root = new TreeItem<>("Exam");
		root.setExpanded(true);
		questionTreeView.setRoot(root);
		//add each question
		for (Question question: MainApp.questionObservableList) {
			TreeItem<String> q = new TreeItem<>(question.getTitle());
			for (String option: question.options) {
				TreeItem<String> o = new TreeItem<>(option);
				q.getChildren().add(o);
			}
			q.setExpanded(true);
			root.getChildren().add(q);
		}
		questionTreeView.setShowRoot(false);
	}

	//BEGIN LEFT SIDE---------------------------------------------------------------------------------------------------
	@FXML private TextField titleField,authorField,numberField;
	@FXML private Label directoryLabel,maxExportLabel;
	@FXML private ChoiceBox<String> formatBox;
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

	/**
	 * General export function
	 *
	 * It handles the export button click.
	 * For each exam to export it gets throws it to the appropiate
	 * function depending on the user selection.
	 */
	@FXML private void export(){
		//clear the answers from any possible previous export
		answers.clear();
		if (directory == null) return;
		int selected = formatBox.getSelectionModel().getSelectedIndex();
		try {
			//Create temporal exam to avoid altering the original
			Exam ex = new Exam(titleField.getText(),authorField.getText());
			for (Question q: MainApp.questionObservableList) {ex.questions.add(q);}
			int n = Integer.parseInt(numberField.getText());
			for (int i = 0; i < n; i++) {
				//shuffle this exam to reorder the questions in a random order
				ex.shuffle();
				//do the correct action depending on the seleceted format
				switch (selected){
					case 0://text
						exportToTextFile(ex,i+1);
						break;
					case 3://console
						exportToConsole(ex,i+1);
						break;
				}
				//add the answers from this exam to the answers
				answers.add(ex.getAnswers());
			}
			//generate the answers file
			BufferedWriter answersFile = new BufferedWriter(new FileWriter(directory+"\\ANSWERS.txt"));
			int s = answers.size();
			for (int i = 0; i < s; i++) {answersFile.write("Version " + (i+1) + ": " + answers.get(i) + "\r\n");}
			answersFile.close();
			//Open the containing folder for the export. Every export
			//will generate files except the console. But console is just for debugging anyways
			Desktop.getDesktop().open(new File(directory));
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

	//SPECIFIC EXPORT FUNCTIONS
	//Each function exports an exam somewhere

	private void exportToConsole(Exam exam, int count){
		System.out.println("------------Export "+count+"----------------------");
		System.out.println(exam.getPretty());
	}

	public void  exportToTextFile(Exam exam,int count){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(directory+"\\Version "+count+".txt"));
			out.write(exam.getPretty());
			out.close();
		}
		catch (IOException e){
			System.out.println("Exception ");
		}
	}

	//factorial function
	private int factorial (int n) {return (n==0) ? 1 : n*factorial(n-1);}

}
