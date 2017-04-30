package com.picassoft.mymedicare;

public class User {

    String email;
    String name;
    String password;
    String gpNumber;
    String colour;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getGpNumber() {
        return gpNumber;
    }

    public void setGpNumber(String gpNumber) {
        this.gpNumber = gpNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
