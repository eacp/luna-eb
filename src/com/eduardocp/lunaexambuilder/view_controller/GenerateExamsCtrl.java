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
import java.util.Arrays;
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

		//check if directory is null
		if (directory == null){
			Dialogs.error("ERROR","Empty folder","You must select a folder first");
			return;
		}

		//check if number is valid
		int selected = formatBox.getSelectionModel().getSelectedIndex();
		if (isNan(numberField.getText())){
			Dialogs.error("ERROR","Information not valid","The number of versions must be a valid number");
			return;
		}
		// is valid
		int n = Integer.parseInt(numberField.getText());

		//Create temporal exam to avoid altering the original
		Exam ex = new Exam(titleField.getText(),authorField.getText());
		ex.questions.addAll(MainApp.questionObservableList);

		//export according to selected format
		for (int i = 0; i < n; i++) {
			//shuffle this exam to reorder the questions in a random order
			ex.shuffle();
			//do the correct action depending on the selected format
			switch (selected){
				case 0://text
					exportToTextFile(ex,i+1);
					break;
				case 1://html
					//this is the first export, create all the pertinent files first
					if (i==0){createNecessaryFilesForHTML();}
					exportToHTML(ex,i+1);
					break;
				case 3://console
					exportToConsole(ex,i+1);
					break;
			}
			//add the answers from this exam to the answers
			answers.add(ex.getAnswers());
		}

		try {
			//generate the answers file
			BufferedWriter answersFile = new BufferedWriter(new FileWriter(directory+"\\ANSWERS.txt"));
			int s = answers.size();
			for (int i = 0; i < s; i++) {answersFile.write("Version " + (i+1) + ": " + answers.get(i) + "\r\n");}
			answersFile.close();

			//Open the containing folder for the export.
			Desktop.getDesktop().open(new File(directory));
		}catch (Exception e){
			e.printStackTrace();
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

	private void  exportToTextFile(Exam exam,int count){
		Data.writeToTextFile(exam.getPretty(),directory+"\\Version "+count+".txt");
	}

	private void exportToHTML(Exam exam,int count){
		//the images and the bootstrap file are ready. render a HTML file
		String template = Data.getTxtResource("exam.html");
		//replace the title and the author
		template = template.replaceAll("#title",exam.title);
		template = template.replaceAll("#author",exam.author);
		//begin creating the list
		List<String> questionListItems = new ArrayList<>(exam.questions.size());
		for (Question q: exam.questions) {
			String li = "<li>\r\n" + q.getTitle() + "\r\n";
			//if the questions has an IMG, add the IMG
			if (q.getImg() != null){
				String imgElement = "<br><img class='img' src='SOURCE'/>";
				//get the file name
				String[] s = q.getImg().split("\\\\");
				String fileName = s[s.length-1];
				//put in in the element
				imgElement=imgElement.replaceAll("SOURCE",fileName);
				//put the element in the li
				li+=imgElement;
			}
			//create an internal ol to pack inside the options
			String internalOl = "<ol type=\"a\">\r\n";
			for (String option: q.options) {
				internalOl+="<li>"+option+"</li>";
			}
			internalOl+="</ol>";
			li+=internalOl;
			li += "</li><br>";
			questionListItems.add(li);
		}
		String joined = String.join("\r\n",questionListItems);
		String html = template.replaceAll("#questions",joined);
		Data.writeToTextFile(html,directory+"\\Version "+count+".html");
	}

	private void createNecessaryFilesForHTML(){
		//create bootstrap.min.css
		Data.writeToTextFile(Data.getTxtResource("bootstrap.min.css"),directory+"\\bootstrap.min.css");
		//create all the images
		for (Question question: MainApp.questionObservableList) {
			if (question.getImg() == null){
				continue;
			}
			//get the file name
			String[] s = question.getImg().split("\\\\");
			System.out.println(s);
			String fileName = s[s.length-1];
			//BEGIN HUGE WTF
			InputStream is = null;OutputStream os = null;
			try {
				System.out.println(fileName);
				is = new FileInputStream(question.getImg());
				os = new FileOutputStream(directory + "\\" + fileName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {os.write(buffer, 0, length);}
				is.close();
				os.close();
			} catch (Exception e){
				System.out.println("CREATE NECESSARY FILES");
			}
			//END HUGE WTF
		}
	}
	//factorial function
	private int factorial (int n) {return (n==0) ? 1 : n*factorial(n-1);}
	//isNan function
	private boolean isNan(String n){try {Integer.parseInt(n);return false;}catch (Exception e){return true;}}
}
