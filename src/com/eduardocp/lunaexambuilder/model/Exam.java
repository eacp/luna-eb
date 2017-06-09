package com.eduardocp.lunaexambuilder.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 06/06/2017.
 */
@XmlRootElement
public class Exam {
	@XmlAttribute
	public String title;
	@XmlAttribute
	public String author;
	@XmlElement(name = "question")
	public List<Question> questions = new ArrayList<>();

	public Exam(){}

	public Exam(String title, String author) {
		this.title = title;
		this.author = author;
	}

	@Override
	public String toString() {
		return "Exam{" +
				"title='" + title + '\'' +
				", author='" + author + '\'' +
				", questions=" + questions +
				'}';
	}

	public void addQuestion(Question question){questions.add(question);}

	public void addQuestion(String title, int correct, String... options){
		questions.add(new Question(title,correct,options));
	}

	public void addQuestion(String title, int value,int correct, String... options){
		questions.add(new Question(title,value,correct,options));
	}
}
