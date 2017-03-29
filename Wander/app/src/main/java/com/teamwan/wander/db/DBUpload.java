package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.teamwan.wander.GameSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


//TODO Replace hardcoded strings by string.xml declaration
public class DBUpload extends AsyncTask<DBpars, Void, Void> {

    private static final String API_URL = "https://script.google.com/macros/s/AKfycbxvbf-dg4ZYc-vFpCCygBgsPpcHl7G31kMmouhhbA6pO-2luQk/exec";
    Context context;

    protected void onPreExecute(){}

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
        UploadObject uploadObject = dbHelper.getUploadObjectsAfter(lastTime);

        Log.i("GSON_TEST", "Printing JSON of GameSessions");
        Gson gson = new Gson();
        String json = gson.toJson(uploadObject);
        Log.i("GSON_TEST", json);

        //TODO: Make connection to server and upload the array

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(API_URL);

        List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("data",json));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvp));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse response = client.execute(post);
            Log.d("uploadResponse",response.getStatusLine().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
