package com.eduardocp.lunaexambuilder.apputils;

import com.eduardocp.lunaexambuilder.model.Exam;

/**
 * Created by Eduardo on 07/06/2017.
 *
 * This class contains example data. The data can be retrieved by invoking the static methods.
 * The purpose of this data is to be used by the example menu.
 */
public class Examples {
	static final String me = "Eduardo A. Castillo Perera";
	public static Exam brands(){
		Exam ex = new Exam("Exam about brands",me);
		ex.addQuestion("What's the brand of this laptop?",1,
				"HP","Dell","Surface (Microsoft)","Apple","Lenovo","Acer","Asus");
		ex.addQuestion("What's cellphone does the developer of this application use?",
				0,"Apple iPhone 6","Samsung Galaxy S8","One Plus 3T","Moto G5");
		ex.addQuestion("Wich of the following watches is the developer of this application using?",
				3,"Rolex","Timex","Nivada","Omega","Tag Heuer");
		ex.addQuestion("What car does the dev drives",4,"BMW",
				"Mercedes Benz","Ferrari","Tesla","He has no car :(");
		return ex;
	}

	public static Exam harryPotter(){
		Exam ex = new Exam("Harry Potter test",me);
		ex.addQuestion("Who married Ron at the end of the saga?",3,"Jane","Luna","Cho","Hermionioe");
		ex.addQuestion("The Chossen One",0,"Harry Potter","Severus Snape","Tom Riddle","Albus Dumbledore");
		ex.addQuestion("Who married HP?",2,"Cho","Luna","Ginny","Tonks","Narcissa");
		ex.addQuestion("Famous Prission",0,"Azkaban","Alkatraz","Hogwards");
		return ex;
	}

	public static Exam doctorWho(){
		Exam ex = new Exam("Doctor Who Test",me);
		ex.addQuestion("The Doctor's wife",2,"Rose","Amy","River","Clara","Donna");
		ex.addQuestion("Planet of the Daleks",1,"Earth","Skaro","Gallifrey","Mars");
		return ex;
	}

	public static Exam supremeExample(){
		Exam br = brands(); Exam hp = harryPotter(); Exam dw = doctorWho();
		Exam supreme = new Exam("SUPREME EXAMPLE",me);
		//Add all the questions from the previous examples
		supreme.questions.addAll(br.questions);
		supreme.questions.addAll(hp.questions);
		supreme.questions.addAll(dw.questions);

		return supreme;
	}
}
