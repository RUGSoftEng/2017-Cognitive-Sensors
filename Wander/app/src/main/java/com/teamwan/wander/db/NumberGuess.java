package com.teamwan.wander.db;

/**
 * This class stores the data generated from a single number press. That includes response
 * time of the player, whether the action done by the player was correct or not, the game session id
 * and the time and number that was tapped on.
 */

public class NumberGuess {

    private long time;
    private long responseTime;
    private boolean isGo;
    private boolean correct;
    private int number;

    public NumberGuess(int number, boolean isGo, long time) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}