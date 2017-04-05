package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.QuestionAnswer;

import java.util.ArrayList;
import java.util.UUID;

public class GameSession{

    private Long time;
    private String gameType;
    private ArrayList<NumberGuess> numberGuesses = new ArrayList<NumberGuess>();
    private ArrayList<QuestionAnswer> questionAnswers = new ArrayList<QuestionAnswer>();
    private int gameSessionId;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

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

    public void addQuestionAnswers(QuestionAnswer qa){
        this.questionAnswers.add(qa);
    }


    //Method to get a universal unique identifier (UUID)
    public synchronized static String getUniqueID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }

        return uniqueID;
    }


    public void save(Context context){

        DBHelper dbHelper = new DBHelper(context);
        //TODO dbHelper insertGameSession should also store the numberGuesses and questionAnswers, without a seperate external call
        int gameSessionID = dbHelper.insertGameSession(this);
        this.setGameSessionId(gameSessionID);
        dbHelper.insertNumberGuesses(this);
        dbHelper.insertQuestionAnswer(this);

    }

}