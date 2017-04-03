package com.teamwan.wander.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
//https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WanderDB.db";

    public static final String GS_TABLE_NAME = "GameSessions";
    public static final String NG_TABLE_NAME = "NumberGuesses";
    public static final String Q_TABLE_NAME = "Questions";
    public static final String QA_TABLE_NAME = "QuestionAnswers";

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
    public static final String Q_COLUMN_TYPE = "QuestionType";
    public static final String Q_COLUMN_MCOPTIONS = "MCOptions";

    public static final String QA_COLUMN_GSID = "GameSessionId";
    public static final String QA_COLUMN_TIME = "Time";
    public static final String QA_COLUMN_ANSWER = "Answer";
    public static final String QA_COLUMN_QID = "QuestionId";

    public Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
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
            Q_COLUMN_QID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Q_COLUMN_QUESTION + " TEXT," +
            Q_COLUMN_TYPE + " TEXT," +
            Q_COLUMN_MCOPTIONS + " TEXT" + ")";

    private static final String CREATE_TABLE_QA = "CREATE TABLE " + QA_TABLE_NAME + "(" +
            QA_COLUMN_GSID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            QA_COLUMN_TIME + " INTEGER," +
            QA_COLUMN_ANSWER + " INTEGER," +
            QA_COLUMN_QID + " INTEGER" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_GS);
        db.execSQL(CREATE_TABLE_NG);
        db.execSQL(CREATE_TABLE_Q);
        db.execSQL(CREATE_TABLE_QA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + GS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Q_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QA_TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    public int insertGameSession(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GS_COLUMN_TIME, gs.getTime());
        contentValues.put(GS_COLUMN_GAMETYPE, gs.getGameType());
        contentValues.put(GS_COLUMN_PLAYERID, gs.getUniqueID(context));
        //Cast without checks, will throw exception when more than MAX_INT gameSessions are played.
        int gameSessionID = (int) db.insert(GS_TABLE_NAME, null, contentValues);
        db.close();
        return gameSessionID;
    }

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

    /*public boolean insertQuestions(GameSession gs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(Question q : gs.getQuestionAnswers()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(QA_COLUMN_GSID, gs.getGameSessionId());
            contentValues.put(QA_COLUMN_QID, qa.getQuestionId());
            db.insert(QA_TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }*/

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

    public UploadObject getUploadObjectsAfter(Long lastTime){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from "+ GS_TABLE_NAME +" where " + GS_COLUMN_TIME +" > " + lastTime + "", null);
        ArrayList<GameSession> gameSessions = new ArrayList<GameSession>();

        if(res.moveToFirst()){
            do{
                GameSession gs = new GameSession(res.getLong(res.getColumnIndex(GS_COLUMN_TIME)),
                                                 res.getString(res.getColumnIndex(GS_COLUMN_GAMETYPE)));
                gs.setGameSessionId(res.getInt(res.getColumnIndex(GS_COLUMN_GSID)));

                Cursor ngs = db.rawQuery("select * from "+ NG_TABLE_NAME +" where " + NG_COLUMN_GSID +" = " + Integer.toString(gs.getGameSessionId()), null);
//                Cursor ngs = db.rawQuery("select * from "+ NG_TABLE_NAME, null);
                //DEBUG SQLiteQuery: select * from NumberGuesses where GameSessionId = 15
                //DEBUG Above SQL statement returns an empty result, whereas the sql statement without the "+'where " + NG_COLUMN_GSID +" = " + gs.getGameSessionId() + """ succesfully returns every numberguess.
                //DEBUG ngs.moveToFirst() is false
                if(ngs.moveToFirst()){
                    do {
                        NumberGuess ng = new NumberGuess(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_NUMBER)),
                                                                    ngs.getInt(ngs.getColumnIndex(NG_COLUMN_ISGO))!=0,
                                                                    ngs.getLong(ngs.getColumnIndex(NG_COLUMN_TIME)));
                        ng.setResponseTime(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_RESPONSETIME)));
                        ng.setCorrect(ngs.getInt(ngs.getColumnIndex(NG_COLUMN_CORRECT))!=0);
                        Log.d("GAMESESSIONID", ""+ngs.getInt(ngs.getColumnIndex(NG_COLUMN_GSID)));


                        Cursor qac = db.rawQuery("select * from "+ QA_TABLE_NAME +" where " + QA_COLUMN_GSID +" = " + Integer.toString(gs.getGameSessionId()), null);
                        if(qac.moveToFirst()){
                            do{
                                QuestionAnswer qa = new QuestionAnswer(qac.getLong(qac.getColumnIndex(QA_COLUMN_TIME)),
                                                                       qac.getInt(qac.getColumnIndex(QA_COLUMN_QID)),
                                                                        qac.getInt(qac.getColumnIndex(QA_COLUMN_ANSWER)));

                                gs.addQuestionAnswers(qa);

                            }while (qac.moveToNext());
                        }

                        gs.addNumberGuess(ng);
                    }while (ngs.moveToNext());
                }
                gameSessions.add(gs);
            }while (res.moveToNext());
        }
        db.close();
        UploadObject uploadObject = new UploadObject(gameSessions.get(0).getUniqueID(context).hashCode(), gameSessions);
        return uploadObject;
    }

//    public boolean updateGameSession(int playerId, Long time, String gameType) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(GS_COLUMN_PLAYERID, playerId);
//        contentValues.put(GS_COLUMN_TIME, time);
//        contentValues.put(GS_COLUMN_GAMETYPE, gameType);
//        db.update(GS_TABLE_NAME, contentValues, GS_COLUMN_GSID +" = ? ", new String[]{GS_COLUMN_GSID});
//        return true;
//    }

//    public Integer deleteGameSession(Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(GS_TABLE_NAME,
//                GS_COLUMN_GSID +" = ? ", id.toString());
//    }

}