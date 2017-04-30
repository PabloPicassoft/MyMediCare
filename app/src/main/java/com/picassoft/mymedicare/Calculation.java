package com.picassoft.mymedicare;

public class Calculation {

    private int foreignUserID;
    private String lBPReading;
    private String hBPReading;
    private String temperatureReading;
    private String heartRateReading;
    private String date;
    private String time;
    private String verdictTemp;
    private String verdictHBP;
    private String verdictLBP;
    private String verdictHR;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



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


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
