package com.diwakar.surveytool.Data;

/**
 * Created by Diwakar on 4/24/2016.
 */
public class Answers {
    private String answerValue;
    private int questionID;
    private int surveyID;
    private String is_single;
    private String answerOption;

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getIs_single() {
        return is_single;
    }

    public void setIs_single(String is_single) {
        this.is_single = is_single;
    }

    public String getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(String answerOption) {
        this.answerOption = answerOption;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }
}
