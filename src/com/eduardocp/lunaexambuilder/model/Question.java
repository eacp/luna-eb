package com.eduardocp.lunaexambuilder.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Eduardo on 06/06/2017.
 *
 * Represents a question.
 */
public class Question {
	private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private String title;
	private int value = 1;
	private int correct;
	@XmlElement(name = "option")
	public List<String> options = new ArrayList<>();

	public Question(){}


	public Question(String title, int correct, String... options) {
		this.title = title;
		this.correct = correct;
		this.options = Arrays.asList(options);
	}

	public Question(String title, int value, int correct, String... options) {
		this(title,correct,options);
		this.value = value;
	}

	@Override
	public String toString() {return title;}

	public String getAnswer(){return ALPHABET[correct] + ") " + options.get(correct);}

	public String getPrettyOptions(){
		String pretty = "";int s = options.size();
		for (int i = 0; i < s; i++) {pretty += ALPHABET[i] + ") " + options.get(i) + "\r\n";}
		return pretty;
	}

	@XmlAttribute
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	@XmlAttribute
	public int getValue() {return value;}
	public void setValue(int value) {this.value = value;}
	@XmlAttribute
	public int getCorrect() {return correct;}
	public void setCorrect(int correct) {this.correct = correct;}
}
