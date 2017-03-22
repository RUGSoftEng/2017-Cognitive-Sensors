package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

        //Timestamp of last time data was uploaded
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        Long lastTime=prefs.getLong("last upload",0);

        //Measure this before data upload, so data generated during the uploading isn't skipped.
        Long newTime = System.currentTimeMillis();

        DBHelper dbHelper= new DBHelper(this.context);
        ArrayList<GameSession> gameSessions = dbHelper.getGameSessionsAfter(lastTime);

        //TODO: Make connection to server and upload the array
        //TODO: Set lastTime to time of update

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
