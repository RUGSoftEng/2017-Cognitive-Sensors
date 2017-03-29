package com.teamwan.wander;

import android.content.Context;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.QuestionAnswer;

import java.util.ArrayList;

public class GameSession{

    private Long time;
    private String gameType;
    private ArrayList<NumberGuess> numberGuesses = new ArrayList<NumberGuess>();
    private ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();
    private int gameSessionId;


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

    public ArrayList<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(ArrayList<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public void save(Context context){

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.insertGameSession(this);
        dbHelper.insertNumberGuesses(this);
    }

}