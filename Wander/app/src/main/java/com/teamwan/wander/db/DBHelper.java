package com.teamwan.wander.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that represents the SQLite database, and contains methods to store and retrieve objects from the database.
 *
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WanderDB.db";

    private static final String GS_TABLE_NAME = "GameSessions";
    private static final String NG_TABLE_NAME = "NumberGuesses";
    private static final String Q_TABLE_NAME = "Questions";
    private static final String QA_TABLE_NAME = "QuestionAnswers";
    private static final String MCQA_TABLE_NAME = "MCQuestionAnswers";

    private static final String GS_COLUMN_GSID = "GameSessionId";
    private static final String GS_COLUMN_PLAYERID = "PlayerId";
    private static final String GS_COLUMN_TIME = "Time";
    private static final String GS_COLUMN_GAMETYPE = "GameType";
    private static final String GS_COLUMN_AVGRESP = "AverageResponse";
    private static final String GS_COLUMN_PR = "Percentage";

    private static final String NG_COLUMN_NGID = "NumberGuessId";
    private static final String NG_COLUMN_GSID = "GameSessionId";
    private static final String NG_COLUMN_TIME = "Time";
    private static final String NG_COLUMN_RESPONSETIME = "ResponseTime";
    private static final String NG_COLUMN_ISGO = "IsGo";
    private static final String NG_COLUMN_CORRECT = "Correct";
    private static final String NG_COLUMN_NUMBER = "Number";

    private static final String Q_COLUMN_QID = "QuestionId";
    private static final String Q_COLUMN_QUESTION = "Question";
    private static final String Q_COLUMN_START = "Start";
    private static final String Q_COLUMN_ONTASK = "OnTask";
    private static final String Q_COLUMN_TYPE = "QuestionType";
    private static final String Q_COLUMN_MCOPTIONS = "MCOptions";
    private static final String Q_COLUMN_NEXT_QUESTION = "NextQuestion";

    private static final String QA_COLUMN_GSID = "GameSessionId";
    private static final String QA_COLUMN_QAID = "QuestionAswerId";
    private static final String QA_COLUMN_TIME = "Time";
    private static final String QA_COLUMN_ANSWER = "Answer";
    private static final String QA_COLUMN_QID = "QuestionId";

    private static final String MCQA_COLUMN_QID = "QuestionId";
    private static final String MCQA_COLUMN_ANSWERNR = "AnswerNumber";
    private static final String MCQA_COLUMN_ANSWER = "Answer";


    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    private static final String CREATE_TABLE_GS = "CREATE TABLE " + GS_TABLE_NAME + "(" +
            GS_COLUMN_GSID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            GS_COLUMN_PLAYERID + " INTEGER," +
            GS_COLUMN_TIME + " INTEGER," +
            GS_COLUMN_AVGRESP + " INTEGER," +
            GS_COLUMN_PR + " FLOAT," +
            GS_COLUMN_GAMETYPE + " TEXT" + ")";

    private static final String CREATE_TABLE_NG = "CREATE TABLE " + NG_TABLE_NAME + "(" +
            NG_COLUMN_NGID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            NG_COLUMN_GSID + " TEXT," +
            NG_COLUMN_TIME + " INTEGER,"+
            NG_COLUMN_RESPONSETIME + " INTEGER," +
            NG_COLUMN_ISGO + " BOOLEAN,"+
            NG_COLUMN_CORRECT + " BOOLEAN,"+
            NG_COLUMN_NUMBER + " INTEGER" + ")";

    private static final String CREATE_TABLE_Q = "CREATE TABLE " + Q_TABLE_NAME + "(" +
            Q_COLUMN_QID + " INTEGER," +
            Q_COLUMN_QUESTION + " TEXT," +
            Q_COLUMN_START + " BOOLEAN," +
            Q_COLUMN_ONTASK + " TEXT," +
            Q_COLUMN_TYPE + " TEXT," +
            Q_COLUMN_MCOPTIONS + " TEXT," +
            Q_COLUMN_NEXT_QUESTION + " TEXT" + ")";

    private static final String CREATE_TABLE_QA = "CREATE TABLE " + QA_TABLE_NAME + "(" +
            QA_COLUMN_GSID + " INTEGER," +
            QA_COLUMN_TIME + " INTEGER," +
            QA_COLUMN_ANSWER + " INTEGER," +
            QA_COLUMN_QAID + " INTEGER," +
            QA_COLUMN_QID + " INTEGER" + ")";

    private static final String CREATE_TABLE_MCQA = "CREATE TABLE " + MCQA_TABLE_NAME + "(" +
            MCQA_COLUMN_QID + " INTEGER," +
            MCQA_COLUMN_ANSWERNR + " INTEGER," +
            MCQA_COLUMN_ANSWER + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) { //create the tables
        db.execSQL(CREATE_TABLE_GS);
        db.execSQL(CREATE_TABLE_NG);
        db.execSQL(CREATE_TABLE_Q);
        db.execSQL(CREATE_TABLE_QA);
        db.execSQL(CREATE_TABLE_MCQA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + GS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Q_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MCQA_TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    /**
     * Inserts the questions into the local database.
     * @param questions - An ArrayList with the questions to add
     */
    private void insertQuestions(ArrayList<Question> questions){
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new Gson();

        for(Question q : questions){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Q_COLUMN_QID, q.getQuestionId());
            contentValues.put(Q_COLUMN_QUESTION, q.getQuestion());
            contentValues.put(Q_COLUMN_START, q.isStart());
            contentValues.put(Q_COLUMN_TYPE, q.getQuestionType());

            String nextQuestionList = gson.toJson(q.getNextQuestion());
            contentValues.put(Q_COLUMN_NEXT_QUESTION, nextQuestionList);

            String onTask = gson.toJson(q.getOnOffTask());
            contentValues.put(Q_COLUMN_ONTASK, onTask);

            if(Objects.equals("MC", q.getQuestionType())) {
                String answers = gson.toJson(q.getAnswers());
                contentValues.put(Q_COLUMN_MCOPTIONS, answers);
            }

            db.insert(Q_TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    /**
     * Empties the questions table and inserts the questions into the local database.
     */
    void overwriteQuestions(ArrayList<Question> questions){
        SQLiteDatabase db = this.getWritableDatabase();
        //Drop and recreate to reset possible auto-increments
        db.execSQL("DROP TABLE IF EXISTS " + Q_TABLE_NAME);
        db.execSQL(CREATE_TABLE_Q);
        db.close();
        insertQuestions(questions);
    }

    /**
     * Returns all questions from the database, including their multiple choice options if applicable.
     */
    public ArrayList<Question> getQuestions(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from "+ Q_TABLE_NAME, null);
        ArrayList<Question> questions = new ArrayList<>();

        Gson gson = new Gson();
        Type intListType = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();

        if(res.moveToFirst()) {
            do {

                String nextQuestion = res.getString(res.getColumnIndex(Q_COLUMN_NEXT_QUESTION));
                ArrayList<Integer> nextQuestionList = gson.fromJson(nextQuestion, intListType);

                String onTaskString = res.getString(res.getColumnIndex(Q_COLUMN_ONTASK));
                ArrayList<Integer> onTask = gson.fromJson(onTaskString, intListType);

                Question q = new Question(res.getInt(res.getColumnIndex(Q_COLUMN_QID)),
                        (res.getInt(res.getColumnIndex(Q_COLUMN_START)) != 0),
                        res.getString(res.getColumnIndex(Q_COLUMN_TYPE)),
                        res.getString(res.getColumnIndex(Q_COLUMN_QUESTION)),
                        nextQuestionList, onTask);
                if (Objects.equals("MC", q.getQuestionType())) {
                    String json = res.getString(res.getColumnIndex(Q_COLUMN_MCOPTIONS));
                    ArrayList<String> answers = gson.fromJson(json, stringListType);
                    q.setAnswers(answers);
                }
                questions.add(q);
            } while (res.moveToNext());
        }
        res.close();
        db.close();
        return questions;
    }

    /**
     * Adds the passed @param gs to the GameSessions table in the local database.
     */
    int insertGameSession(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GS_COLUMN_TIME, gs.getTime());
        contentValues.put(GS_COLUMN_AVGRESP, gs.getAvg());
        contentValues.put(GS_COLUMN_PR, gs.getPercentage());
        contentValues.put(GS_COLUMN_GAMETYPE, gs.getGameType());
        contentValues.put(GS_COLUMN_PLAYERID, GameSession.getUniqueID(context));
        //Cast without checks, will throw exception when more than MAX_INT gameSessions are played.
        int gameSessionID = (int) db.insert(GS_TABLE_NAME, null, contentValues);
        db.close();
        return gameSessionID;
    }

    /**
     * Adds the numberGuesses from the passed GameSessiosn @param gs to the numberGuesses table in the local database.
     */
    void insertNumberGuesses(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(NumberGuess ng : gs.getNumberGuesses()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NG_COLUMN_GSID, gs.getGameSessionId());
            contentValues.put(NG_COLUMN_TIME, ng.getTime());
            contentValues.put(NG_COLUMN_RESPONSETIME, ng.getResponseTime());
            contentValues.put(NG_COLUMN_ISGO, ng.isGo());
            contentValues.put(NG_COLUMN_CORRECT, ng.isCorrect());
            contentValues.put(NG_COLUMN_NUMBER, ng.getNumber());
            db.insert(NG_TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    /**
     * Adds the question answers from the passed GameSessiosn @param gs to the questionAnswers table in the local database.
     */
    void insertQuestionAnswer(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(QuestionAnswer qa : gs.getQuestionAnswers()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(QA_COLUMN_GSID, gs.getGameSessionId());
            contentValues.put(QA_COLUMN_QID, qa.getQuestionId());
            contentValues.put(QA_COLUMN_TIME, qa.getTime());
            contentValues.put(QA_COLUMN_ANSWER, qa.getAnswer());
            db.insert(QA_TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    /**
     * Returns an ArrayList<GameSession> which contains all gameSessions after a certain timestamp.
     */
    public ArrayList<GameSession> getGameSessionsAfter(Long lastTime){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from "+ GS_TABLE_NAME +" where " + GS_COLUMN_TIME +" > " + lastTime + "", null);
        ArrayList<GameSession> gameSessions = new ArrayList<>();

        if(res.moveToFirst()){
            do{
                GameSession gs = new GameSession(res.getLong(res.getColumnIndex(GS_COLUMN_TIME)),
                        res.getString(res.getColumnIndex(GS_COLUMN_GAMETYPE)));
                gs.setGameSessionId(res.getInt(res.getColumnIndex(GS_COLUMN_GSID)));
                gs.setPercentage(res.getFloat(res.getColumnIndex(GS_COLUMN_PR)));
                Cursor ngs = db.rawQuery("select * from "+ NG_TABLE_NAME +" where " + NG_COLUMN_GSID +" = " + Integer.toString(gs.getGameSessionId()), null);
                if(ngs.moveToFirst()){
                    do {
                        NumberGuess ng = new NumberGuess(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_NUMBER)),
                                ngs.getInt(ngs.getColumnIndex(NG_COLUMN_ISGO))!=0,
                                ngs.getLong(ngs.getColumnIndex(NG_COLUMN_TIME)));
                        ng.setResponseTime(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_RESPONSETIME)));
                        ng.setCorrect(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_CORRECT))!=0);

                        gs.addNumberGuess(ng);
                    }while (ngs.moveToNext());
                }
                ngs.close();

                Cursor qac = db.rawQuery("select * from "+ QA_TABLE_NAME +" where " + QA_COLUMN_GSID +" = " + Integer.toString(gs.getGameSessionId()), null);
                if(qac.moveToFirst()){
                    do{
                        QuestionAnswer qa = new QuestionAnswer(qac.getLong(qac.getColumnIndex(QA_COLUMN_TIME)),
                                qac.getInt(qac.getColumnIndex(QA_COLUMN_QID)),
                                qac.getInt(qac.getColumnIndex(QA_COLUMN_ANSWER)));

                        gs.addQuestionAnswer(qa);

                    }while (qac.moveToNext());
                }
                qac.close();
                gameSessions.add(gs);
            }while (res.moveToNext());
        }
        res.close();
        db.close();
        if(gameSessions.size() == 0){
            return null;
        }
        return gameSessions;
    }

    /**
     * Fetches an uploadObject which contains all gameSessions after the passed @param lastTime from the local database. The playerID is passed on as well, so that the uploadObject contains every field needed to send to the server.
     */
    UploadObject getUploadObjectAfter(Long lastTime){
        ArrayList<GameSession> gameSessions = getGameSessionsAfter(lastTime);
        if(gameSessions.size() == 0) {
            return null;
        } else {
            return new UploadObject(GameSession.getUniqueID(context).hashCode(), gameSessions);
        }
    }
}