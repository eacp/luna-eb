package com.eduardocp.lunaexambuilder.utils;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
public class Dialogs {
	//INFO
	public static void info(String title,String header,String body){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		alert.showAndWait();
	}
	
	public static void info(String title, String body){
		info(title,null,body);
	}

	//warning
	public static void warning(String title,String header,String body){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		alert.showAndWait();
	}
	
	public static void warning(String title, String body){
		warning(title,null,body);
	}

	//error
	public static void error(String title,String header,String body){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		alert.showAndWait();
	}
	
	public static void error(String title, String body){
		error(title,null,body);
	}

	public static boolean confirm(String title,String header,String body){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}
	
	public static String prompt(String title,String header,String body){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(body);
		
		Optional<String> result = dialog.showAndWait();
		
		return result.isPresent() ? result.get() : "";
	}
	
	public static String prompt(String title,String body){
		return prompt(title,null,body);
	}
}