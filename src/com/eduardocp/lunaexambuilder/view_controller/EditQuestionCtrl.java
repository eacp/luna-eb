package com.eduardocp.lunaexambuilder.view_controller;

import com.eduardocp.lunaexambuilder.MainApp;
import com.eduardocp.lunaexambuilder.model.Question;
import com.eduardocp.lunaexambuilder.utils.Data;
import com.eduardocp.lunaexambuilder.utils.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * Created by Eduardo on 06/06/2017.
 */
public class EditQuestionCtrl{
	public Stage dialogStage;
	public Question question;
	public boolean okClicked = false;

	private ObservableList<String> optionsList = FXCollections.observableArrayList();

	@FXML private TextField textField;
	@FXML private TextField valueField;
	@FXML private ChoiceBox<String> correctBox;
	@FXML private ListView<String> optionsView;
	@FXML private Label img;
	//TOOLBAR
	@FXML private TextField newOptionField;

	@FXML
	private void addOption(){
		String t = newOptionField.getText();
		if (t.length() < 1) return;
		optionsList.add(t);
		newOptionField.setText("");
	}

	@FXML
	private void deleteOption(){
		int index = optionsView.getSelectionModel().getSelectedIndex();
		if (index < 0) return;
		optionsList.remove(index);
	}

	@FXML
	private void initialize(){
		optionsView.setItems(optionsList);
		correctBox.setItems(optionsList);
		newOptionField.setOnAction(e->addOption());
	}

	public void setQuestion(Question question){
		this.question = question;
		//fill the form
		textField.setText(question.getTitle());
		valueField.setText(Integer.toString(question.getValue()));
		optionsList.setAll(question.options);
		correctBox.getSelectionModel().select(question.getCorrect());
		img.setText(question.getImg());

	}

	@FXML
	private void ok(){
		String text = textField.getText();
		String valueString = valueField.getText();
		int correct = correctBox.getSelectionModel().getSelectedIndex();
		//Validate data and provide feedback to the user if necessary
		if (text == null || !isNumeric(valueString) || correct < 0 || optionsList.size() < 2){
			Dialogs.error("Error", "Invalid Data", "The data is not valid. Please verify all the fields." +
					" The question must contain at least 2 options");
			return;
		}
		int value = Integer.parseInt(valueString);
		//create question object
		Question q = new Question(text,value,correct,"");
		//add the options
		q.options = optionsList;
		//add the image path
		q.setImg(img.getText());

		//if the user is editing a question, update , else add new question
		if (MainApp.currentEditingQuestionIndex < 0){
			MainApp.questionObservableList.add(q);
		}else {
			MainApp.questionObservableList.set(MainApp.currentEditingQuestionIndex,q);
		}
		okClicked = true;
		MainApp.currentExam.questions = MainApp.questionObservableList;
		dialogStage.close();
	}

	@FXML private void chooseImage(){
		String imagePath = Data.getFilePath(dialogStage);
		img.setText(imagePath);
	}

	@FXML
	private void cancel(){dialogStage.close();}
	//validate an integer
	public static boolean isNumeric(String str){
		try{int i = Integer.parseInt(str);}catch(NumberFormatException nfe){return false;}return true;
	}
}
