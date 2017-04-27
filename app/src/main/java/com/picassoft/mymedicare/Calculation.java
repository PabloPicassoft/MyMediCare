package com.picassoft.mymedicare;

/**
 * Created by Paul on 27/04/2017.
 */

public class Calculation {

    int foreignUserID;
    String lBPReading;
    String hBPReading;
    String temperatureReading;
    String heartRateReading;

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    String dateAndTime;

    public int getForeignUserID() {
        return foreignUserID;
    }

    public void setForeignUserID(int foreignUserID) {
        this.foreignUserID = foreignUserID;
    }



    public String gethBPReading() {
        return hBPReading;
    }

    public void sethBPReading(String hBPReading) {
        this.hBPReading = hBPReading;
    }

    public String getVerdictTemp() {
        return verdictTemp;
    }

    public void setVerdictTemp(String verdictTemp) {
        this.verdictTemp = verdictTemp;
    }

    public String getVerdictHBP() {
        return verdictHBP;
    }

    public void setVerdictHBP(String verdictHBP) {
        this.verdictHBP = verdictHBP;
    }

    public String getVerdictLBP() {
        return verdictLBP;
    }

    public void setVerdictLBP(String verdictLBP) {
        this.verdictLBP = verdictLBP;
    }

    public String getVerdictHR() {
        return verdictHR;
    }

    public void setVerdictHR(String verdictHR) {
        this.verdictHR = verdictHR;
    }

    String verdictTemp, verdictHBP, verdictLBP, verdictHR;



    public String getlBPReading() {
        return lBPReading;
    }

    public void setlBPReading(String lBPReading) {
        this.lBPReading = lBPReading;
    }

    public String getTemperatureReading() {
        return temperatureReading;
    }

    public void setTemperatureReading(String temperatureReading) {
        this.temperatureReading = temperatureReading;
    }

    public String getHeartRateReading() {
        return heartRateReading;
    }

    public void setHeartRateReading(String heartRateReading) {
        this.heartRateReading = heartRateReading;
    }


}
