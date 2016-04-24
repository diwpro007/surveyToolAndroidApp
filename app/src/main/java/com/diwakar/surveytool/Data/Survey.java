package com.diwakar.surveytool.Data;

import java.io.Serializable;

/**
 * Created by Diwakar on 4/23/2016.
 */
public class Survey implements Serializable {
    private int surveyID;
    private String surveyName;
    private Question[] questions;
    private int totalOnlineData;
    private int totalOfflineData;

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public void setTotalOnlineData(int totalOnlineData) {
        this.totalOnlineData = totalOnlineData;
    }

    public int getTotalOfflineData() {
        return totalOfflineData;
    }

    public void setTotalOfflineData(int totalOfflineData) {
        this.totalOfflineData = totalOfflineData;
    }
}
