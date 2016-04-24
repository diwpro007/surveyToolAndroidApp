package com.diwakar.surveytool.Data;

import java.io.Serializable;

/**
 * Created by Diwakar on 4/23/2016.
 */
public class Option implements Serializable {
    private int optionID;
    private String optionName;

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
