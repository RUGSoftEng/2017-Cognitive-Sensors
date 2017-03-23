package com.teamwan.wander;

import android.content.Context;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;

import java.util.ArrayList;

public class GameSession{

    private int gameSessionId;
    private int playerId;
    private Long time;
    private String gameType;
    private ArrayList<NumberGuess> numberGuesses = new ArrayList<NumberGuess>();
    private ArrayList<String> questions = new ArrayList<String>();

    public ArrayList<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Integer> answers) {
        this.answers = answers;
    }

    private ArrayList<Integer> answers = new ArrayList<Integer>();


    public GameSession(Long time, String gameType) {
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
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

    public void addNumberGuess(NumberGuess ng){
        this.numberGuesses.add(ng);
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public void save(Context context){

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.insertGameSession(this);

        for(NumberGuess ng: numberGuesses){
            dbHelper.insertNumberGuess(ng);
        }
    }

}