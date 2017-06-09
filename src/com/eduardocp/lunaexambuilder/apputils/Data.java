package com.eduardocp.lunaexambuilder.apputils;

import java.io.File;

import com.eduardocp.lunaexambuilder.MainApp;
import com.eduardocp.lunaexambuilder.model.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Created by Eduardo on 06/06/2017.
 */
public class Data {
	//SAVE-----------------------------------------------------------------------------------------
	public static void saveExamToFile(File file,Exam exam){
		try {
			//prepare the marshaller
			JAXBContext context = JAXBContext.newInstance(Exam.class);
			Marshaller m = context.createMarshaller();
			//Save the exam to a file and print it pretty on the console
			m.marshal(exam,file);
			//prettify here to save space in the file
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			System.out.println(exam);
			m.marshal(exam,System.out);
			MainApp.currentFilePath = file.getAbsolutePath();

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void saveExamToFile(String file,Exam exam){saveExamToFile(new File(file),exam);}

	//LOAD-----------------------------------------------------------------------------------------
	public static Exam loadExamFromFile(File file){
		Exam ex = new Exam("","");
		try {
			JAXBContext context = JAXBContext.newInstance(Exam.class);
			Unmarshaller um = context.createUnmarshaller();
			Exam exam = (Exam) um.unmarshal(file);
			ex = exam;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ex;
	}

	public static Exam loadExamFromFile(String file){return loadExamFromFile(new File(file));}

	//LOAD FROM SELECTED FILE
	public static Exam loadFromOpenExamFile(Stage ps){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML and EXAM files (*.xml,*.exam)", "*.xml","*.exam");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(ps);
		MainApp.currentFilePath = file.getAbsolutePath();
		System.out.println(MainApp.currentFilePath);
		return loadExamFromFile(file);
	}

	//SELECT A FILE AND SAVE TO THAT FILE
	public static void saveToSelectedFile(Stage ps,Exam exam){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML and EXAM files (*.xml,*.exam)", "*.xml","*.exam");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showSaveDialog(ps);
		saveExamToFile(file,exam);

	}

	//SELECT A DIRECTORY AND GET ITS PATH
	public static String getDirectory(Stage ps){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(ps);
		//prevent nullPointerException by returning an empty String
		return selectedDirectory == null ? "" :selectedDirectory.getAbsolutePath();
	}

}