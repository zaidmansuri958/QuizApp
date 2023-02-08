package com.zaidMansuri.quizapp;

public class AuthModal {
    String name,email,number,password;
    int score;

    public AuthModal(String name, String email, String number, String password,int score) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public AuthModal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
