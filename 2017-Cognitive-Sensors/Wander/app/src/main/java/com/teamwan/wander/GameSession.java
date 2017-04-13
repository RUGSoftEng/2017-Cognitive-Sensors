package com.teamwan.wander;

import java.util.ArrayList;

public class GameSession{

    private int gameSessionId;
    private int playerId;
    private String time;
    private String gameType;
    private ArrayList<NumberGuess> numberGuesses;
    private ArrayList<String> questions;
    private  int answer;


    public GameSession(String time, String gameType) {
        this.time = time;
        this.gameType = gameType;
    }

    public int getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(int gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public ArrayList<NumberGuess> getNumberGuesses() {
        return numberGuesses;
    }

    public void setNumberGuesses(ArrayList<NumberGuess> numberGuesses) {
        this.numberGuesses = numberGuesses;
    }

    public void AddNumberGuess(NumberGuess ng){
        this.numberGuesses.add(ng)
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void save(){

    }

}