package com.diwakar.surveytool.Data;

import java.io.Serializable;

/**
 * Created by Diwakar on 4/23/2016.
 */
public class Question implements Serializable {
    private int questionID;
    private String questionName;
    private String answerType;
    private String answerIPType;
    private boolean mandatory;
    private Option[] options;

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getAnswerIPType() {
        return answerIPType;
    }

    public void setAnswerIPType(String answerIPType) {
        this.answerIPType = answerIPType;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        this.options = options;
    }
}
