package com.teamwan.wander;

public class NumberGame {

    private int number;
    private boolean isGo;
    private boolean correct;
    private long responseTime;
    private String time;


    public NumberGame(int number, boolean isGo, String time) {
        this.number = number;
        this.isGo = isGo;
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isGo() {
        return isGo;
    }

    public void setGo(boolean go) {
        isGo = go;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberGuessId() {
        return numberGuessId;
    }

    public void setNumberGuessId(int numberGuessId) {
        this.numberGuessId = numberGuessId;
    }
}