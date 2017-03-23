package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.teamwan.wander.GameSession;
import com.teamwan.wander.R;

import java.util.ArrayList;


//TODO Replace hardcoded strings by string.xml declaration
public class DBUpload extends AsyncTask<DBpars, Void, Void> {

    Context context;

    protected void onPreExecute(){
    }

    @Override
    protected Void doInBackground(DBpars... params) {
        this.context = params[0].context;
        Log.i("DBUPLOAD_DOINBACKGROUND", "DBUpload.doInBackground() executing");
        //Timestamp of last time data was uploaded
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        Long lastTime=prefs.getLong("last upload",0);

        //Measure this before data upload, so data generated during the uploading isn't skipped.
        Long newTime = System.currentTimeMillis();

        DBHelper dbHelper= new DBHelper(this.context);
        ArrayList<GameSession> gameSessions = dbHelper.getGameSessionsAfter(lastTime);

        Log.i("GSON_TEST", "Printing JSON of GameSessions");
        Gson gson = new Gson();
        String json = gson.toJson(gameSessions);
        Log.i("GSON_TEST", json);

        //TODO: Make connection to server and upload the array


        //Update lastTime after updating
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("last upload", newTime);
        editor.apply();
        return null;
    }

    protected void onPostExecute(String result) {
        Toast.makeText(this.context, "Succesfully uploaded data", Toast.LENGTH_SHORT).show();
    }
}
