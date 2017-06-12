package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.QuestionAnswer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents a game session object.
 *
 * In this class the player id is stored alongside of which game was played at what certain
 * point of time. Together this three types of data create a unique game session which
 * will be represented as game session id in the other tables of the database.
 */

public class GameSession{

    private Long time;
    private String gameType;


    private float percentage;
    private ArrayList<NumberGuess> numberGuesses = new ArrayList<>();
    private ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();
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

    public void setPercentage(float percentage) {
        this.percentage = percentage;
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

    public int getAvg(){
        int average = 0;
        int counter = 0;

        for(NumberGuess n: numberGuesses){
            if(n.getResponseTime() != 0) {
                average += n.getResponseTime();
                counter++;
            }
        }
        average= counter == 0 ? 0 : average / counter;

        return average;
    }

    public float getPercentage(){
        return percentage;
    }
    public void addQuestionAnswer(QuestionAnswer qa){
        this.questionAnswers.add(qa);
    }

    /**
     * getUniqueID creates an universally unique identifier (UUID), which is used for representing the playerID
     */
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

    /**
     * Saves this gameSession into the local database, during which the local gameSessionID is generated, which is used when saving the data in the other tables.
     */
    public void save(Context context){
        DBHelper dbHelper = new DBHelper(context);
        int gameSessionID = dbHelper.insertGameSession(this);
        this.setGameSessionId(gameSessionID);
        dbHelper.insertNumberGuesses(this);
        dbHelper.insertQuestionAnswer(this);
    }

}