package com.teamwan.wander.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public static final String DATABASE_NAME = "WanderDB.db";

    public static final String GS_TABLE_NAME = "GameSessions";
    public static final String NG_TABLE_NAME = "NumberGuesses";
    public static final String Q_TABLE_NAME = "Questions";
    public static final String QA_TABLE_NAME = "QuestionAnswers";
    public static final String MCQA_TABLE_NAME = "MCQuestionAnswers";

    public static final String GS_COLUMN_GSID = "GameSessionId";
    public static final String GS_COLUMN_PLAYERID = "PlayerId";
    public static final String GS_COLUMN_TIME = "Time";
    public static final String GS_COLUMN_GAMETYPE = "GameType";

    public static final String NG_COLUMN_NGID = "NumberGuessId";
    public static final String NG_COLUMN_GSID = "GameSessionId";
    public static final String NG_COLUMN_TIME = "Time";
    public static final String NG_COLUMN_RESPONSETIME = "ResponseTime";
    public static final String NG_COLUMN_ISGO = "IsGo";
    public static final String NG_COLUMN_CORRECT = "Correct";
    public static final String NG_COLUMN_NUMBER = "Number";

    public static final String Q_COLUMN_QID = "QuestionId";
    public static final String Q_COLUMN_QUESTION = "Question";
    public static final String Q_COLUMN_START = "Start";
    public static final String Q_COLUMN_TYPE = "QuestionType";
    public static final String Q_COLUMN_MCOPTIONS = "MCOptions";

    public static final String QA_COLUMN_GSID = "GameSessionId";
    public static final String QA_COLUMN_TIME = "Time";
    public static final String QA_COLUMN_ANSWER = "Answer";
    public static final String QA_COLUMN_QID = "QuestionId";

    public static final String MCQA_COLUMN_QID = "QuestionId";
    public static final String MCQA_COLUMN_ANSWERNR = "AnswerNumber";
    public static final String MCQA_COLUMN_ANSWER = "Answer";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    private static final String CREATE_TABLE_GS = "CREATE TABLE " + GS_TABLE_NAME + "(" +
            GS_COLUMN_GSID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            GS_COLUMN_PLAYERID + " INTEGER," +
            GS_COLUMN_TIME + " INTEGER," +
            GS_COLUMN_GAMETYPE + " TEXT" + ")";

    private static final String CREATE_TABLE_NG = "CREATE TABLE " + NG_TABLE_NAME + "(" +
            NG_COLUMN_NGID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            NG_COLUMN_GSID + " TEXT," +
            NG_COLUMN_TIME + " INTEGER,"+
            NG_COLUMN_RESPONSETIME + " INTEGER," +
            NG_COLUMN_ISGO + " BOOLEAN,"+
            NG_COLUMN_CORRECT + " BOOLEAN,"+
            NG_COLUMN_NUMBER + " INTEGER" +")";

    private static final String CREATE_TABLE_Q = "CREATE TABLE " + Q_TABLE_NAME + "(" +
            Q_COLUMN_QID + " INTEGER," +
            Q_COLUMN_QUESTION + " TEXT," +
            Q_COLUMN_START + " BOOLEAN," +
            Q_COLUMN_TYPE + " TEXT," +
            Q_COLUMN_MCOPTIONS + " TEXT" + ")";

    private static final String CREATE_TABLE_QA = "CREATE TABLE " + QA_TABLE_NAME + "(" +
            QA_COLUMN_GSID + " INTEGER," +
            QA_COLUMN_TIME + " INTEGER," +
            QA_COLUMN_ANSWER + " INTEGER," +
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
     * @param questions
     */
    public void insertQuestions(ArrayList<Question> questions){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Question q : questions){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Q_COLUMN_QID, q.getQuestionId());
            contentValues.put(Q_COLUMN_QUESTION, q.getQuestion());
            contentValues.put(Q_COLUMN_START, q.isStart());
            contentValues.put(Q_COLUMN_TYPE, q.getQuestionType());
            if(Objects.equals("MC", q.getQuestionType())) {
                Gson gson = new Gson();
                String answers = gson.toJson(q.getAnswers());
                contentValues.put(Q_COLUMN_MCOPTIONS, answers);
            }
            db.insert(Q_TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    /**
     * Empties the questions table and inserts the questions into the local database.
     * @param questions
     */
    public void overwriteQuestions(ArrayList<Question> questions){
        SQLiteDatabase db = this.getWritableDatabase();
        //Drop and recreate to reset possible auto-increments
        db.execSQL("DROP TABLE IF EXISTS " + Q_TABLE_NAME);
        db.execSQL(CREATE_TABLE_Q);
        db.close();
        insertQuestions(questions);
    }

    /**
     * Returns all questions from the database, including their multiple choice options if applicable.
     * @return
     */
    public ArrayList<Question> getQuestions(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from "+ Q_TABLE_NAME, null);
        ArrayList<Question> questions = new ArrayList<>();

        if(res.moveToFirst()) {
            do {
                Question q = new Question(res.getInt(res.getColumnIndex(Q_COLUMN_QID)),
                        (res.getInt(res.getColumnIndex(Q_COLUMN_START)) != 0),
                        res.getString(res.getColumnIndex(Q_COLUMN_TYPE)),
                        res.getString(res.getColumnIndex(Q_COLUMN_QUESTION)));
                if (Objects.equals("MC", q.getQuestionType())) {
                    Gson gson = new Gson();
                    String json = res.getString(res.getColumnIndex(Q_COLUMN_MCOPTIONS));
                    Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                    ArrayList<String> answers = gson.fromJson(json, listType);

                    q.setAnswers(answers);
                }
                questions.add(q);
            } while (res.moveToNext());
        }
        res.close();
        return questions;
    }

    /**
     * Adds the passed @param gs to the GameSessions table in the local database.
     * @param gs
     * @return
     */
    public int insertGameSession(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GS_COLUMN_TIME, gs.getTime());
        contentValues.put(GS_COLUMN_GAMETYPE, gs.getGameType());
        contentValues.put(GS_COLUMN_PLAYERID, GameSession.getUniqueID(context));
        //Cast without checks, will throw exception when more than MAX_INT gameSessions are played.
        int gameSessionID = (int) db.insert(GS_TABLE_NAME, null, contentValues);
        db.close();
        return gameSessionID;
    }

    /**
     * Adds the numberGuesses from the passed GameSessiosn @param gs to the numberGuesses table in the local database.
     * @param gs
     * @return
     */
    public boolean insertNumberGuesses(GameSession gs) {
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
        return true;
    }

    /**
     * Adds the question answers from the passed GameSessiosn @param gs to the questionAnswers table in the local database.
     * @param gs
     * @return
     */
    public boolean insertQuestionAnswer(GameSession gs) {
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
        return true;
    }

    /**
     * Returns an ArrayList<GameSession> which contains all gameSessions after a certain timestamp.
     * @param lastTime
     * @return
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

                Cursor ngs = db.rawQuery("select * from "+ NG_TABLE_NAME +" where " + NG_COLUMN_GSID +" = " + Integer.toString(gs.getGameSessionId()), null);
                if(ngs.moveToFirst()){
                    do {
                        NumberGuess ng = new NumberGuess(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_NUMBER)),
                                ngs.getInt(ngs.getColumnIndex(NG_COLUMN_ISGO))!=0,
                                ngs.getLong(ngs.getColumnIndex(NG_COLUMN_TIME)));
                        ng.setResponseTime(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_RESPONSETIME)));
                        ng.setCorrect(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_CORRECT))!=0);
                        Log.d("GAMESESSIONID", ""+ngs.getInt(ngs.getColumnIndex(NG_COLUMN_GSID)));

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
     * @param lastTime
     * @return
     */
    public UploadObject getUploadObjectAfter(Long lastTime){
        ArrayList<GameSession> gameSessions = getGameSessionsAfter(lastTime);
        if(gameSessions.size() == 0) {
            return null;
        } else {
            UploadObject uploadObject = new UploadObject(GameSession.getUniqueID(context).hashCode(), gameSessions);
            return uploadObject;
        }
    }
}