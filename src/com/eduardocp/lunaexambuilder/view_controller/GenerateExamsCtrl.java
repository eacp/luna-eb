package com.eduardocp.lunaexambuilder.view_controller;

import com.eduardocp.lunaexambuilder.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Created by Eduardo on 08/06/2017.
 */
public class GenerateExamsCtrl {
	@FXML
	private void goToOverview() throws IOException {
		//Get the current width and height to make the transition consistent
		double w = MainApp.window.getScene().getWidth(),h = MainApp.window.getScene().getHeight();
		//load the FXML
		Parent root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		//Set the scene in the window
		MainApp.window.setScene(new Scene(root,w,h));
	}
}
