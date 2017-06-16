package com.eduardocp.lunaexambuilder.view_controller;

import com.eduardocp.lunaexambuilder.MainApp;
import com.eduardocp.lunaexambuilder.utils.*;
import com.eduardocp.lunaexambuilder.model.*;
import com.eduardocp.lunaexambuilder.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewCtrl implements Initializable{
	private Image noImage;

	//BEGIN TABLE-------------------------------------------------------------------------------------------------------
	@FXML private TableView<Question> questionTable;
	//BEGIN TABLE COLUMNS--------------------------------------------------------------------------------------------
	@FXML private TableColumn<Question,String> titleColumn,answerColumn;
	@FXML private TableColumn<Question,Integer> valueColumn;
	//END TABLE COLUMNS----------------------------------------------------------------------------------------------
	//END TABLE---------------------------------------------------------------------------------------------------------

	//BEGIN DETAIL AREA-------------------------------------------------------------------------------------------------
	@FXML private Label titleLabel, valueLabel, answerLabel;
	@FXML private ImageView image;
	@FXML private TextArea optionsArea;
	//END DETAIL AREA---------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		noImage = new Image(MainApp.class.getResourceAsStream("resources/no-image.jpg"));
		//POPULATE THE TABLE
		questionTable.setItems(MainApp.questionObservableList);
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		//set selection event
		questionTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showQuestion(newValue));
		System.out.println(resources);
	}

	private void showQuestion(Question question){
		if (question != null){
			titleLabel.setText(question.getTitle());
			valueLabel.setText(Integer.toString(question.getValue()));
			optionsArea.setText(question.getPrettyOptions());
			answerLabel.setText(question.getAnswer());
			try {
				//if there is a valid image, put the valid image
				image.setImage(new Image(new FileInputStream(question.getImg())));
			}catch (Exception e){
				//put the empty image
				image.setImage(noImage);
				System.out.println("Object has no valid img");
			}
		}else{
			titleLabel.setText("");
			valueLabel.setText("");
			optionsArea.setText("");
			answerLabel.setText("");
		}
	}

	//BEGIN MENU BAR----------------------------------------------------------------------------------------------------
	@FXML private void open(){
		Stage ps = MainApp.window;
		Exam e = Data.loadFromOpenExamFile(ps);
		MainApp.currentExam = e;
		MainApp.questionObservableList.setAll(e.questions);
		changeTitle(MainApp.currentFilePath.substring(MainApp.currentFilePath.lastIndexOf('\\') + 1) + " - " + MainApp.APP_TITLE);
	}

	@FXML private void save(){
		Exam ex = MainApp.currentExam;
		ex.questions = MainApp.questionObservableList;
		if (MainApp.currentFilePath == ""){saveAs();}
		else{
			Data.saveExamToFile(MainApp.currentFilePath,ex);
			changeTitle(MainApp.currentFilePath.substring(MainApp.currentFilePath.lastIndexOf('\\') + 1) + " - " + MainApp.APP_TITLE);
		}
	}

	@FXML private void saveAs(){
		Stage ps = MainApp.window;
		Exam ex = MainApp.currentExam;
		Data.saveToSelectedFile(ps,ex);
		changeTitle(MainApp.currentFilePath.substring(MainApp.currentFilePath.lastIndexOf('\\') + 1) + " - " + MainApp.APP_TITLE);
	}

	//BEGIN LOAD EXAMPLES--------------------------------------------------------------------------------------------
	//This function load an specific example exam and puts its questions on the table
	private void loadExample(Exam example){
		MainApp.currentExam = example;
		MainApp.questionObservableList.setAll(example.questions);
	}

	//These are the buttons
	@FXML private void loadBrandsExample(){loadExample(Examples.brands());}
	@FXML private void loadHarryPotterExample(){loadExample(Examples.harryPotter());}
	@FXML private void loadDoctorWhoExamples(){loadExample(Examples.doctorWho());}
	@FXML private void loadSupremeExample(){loadExample(Examples.supremeExample());}
	//END LOAD EXAMPLES----------------------------------------------------------------------------------------------

	//BEGIN EXPORT---------------------------------------------------------------------------------------------------
	@FXML private void goToGenerateExams() throws IOException {
		//Make the user save the file first
		if (MainApp.currentFilePath == "") saveAs();
		//Get the current width and height to make the transition consistent
		double w = MainApp.window.getScene().getWidth(),h = MainApp.window.getScene().getHeight();
		//load the FXML
		Parent root = FXMLLoader.load(getClass().getResource("GenerateExams.fxml"));
		//Set the scene in the window and the appropiate title
		changeTitle("Generate exams");
		MainApp.window.setScene(new Scene(root,w,h));
	}
	//END EXPORT-----------------------------------------------------------------------------------------------------

	//END MENU BAR------------------------------------------------------------------------------------------------------

	//BEGIN QUESTION EDITOR---------------------------------------------------------------------------------------------
	@FXML private void delete(){
		int index = questionTable.getSelectionModel().getSelectedIndex();
		if (index < 0){return;}
		if (Dialogs.confirm("Confirmation","Confirm delete","Do you really want to remove this question?")){
			questionTable.getItems().remove(index);
		}
	}

	@FXML private void edit(){
		Stage ps = (Stage) questionTable.getScene().getWindow();
		int index = questionTable.getSelectionModel().getSelectedIndex();
		if (index < 0){return;}
		MainApp.currentEditingQuestionIndex = index;
		Question q = questionTable.getSelectionModel().getSelectedItem();
		boolean okClicked = MainApp.showEditQuestionDialog(ps,q,"Edit question: " + q.getTitle());
		if (okClicked) showQuestion(questionTable.getItems().get(index));
	}

	@FXML private void newQuestion(){
		Stage ps = (Stage) questionTable.getScene().getWindow();
		MainApp.currentEditingQuestionIndex = -1;
		boolean okClicked = MainApp.showEditQuestionDialog(ps,new Question(),"New question");
		if (okClicked) showQuestion(questionTable.getItems().get(questionTable.getItems().size() - 1));
	}
	//END QUESTION EDITOR-----------------------------------------------------------------------------------------------

	//This function just changes the window title
	private void changeTitle(String title){MainApp.window.setTitle(title);}
}
