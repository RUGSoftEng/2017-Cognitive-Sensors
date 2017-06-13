package com.teamwan.wander.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.teamwan.wander.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Task that can be executed to upload every new gameSession since last upload.
 */

public class DBUpload extends AsyncTask<DBpars, Void, Void> {

    private static final String API_PARAM = "data";
    private static final String LAST_UPLOAD = "last upload";

    protected void onPreExecute(){}

    @Override
    protected Void doInBackground(DBpars... params) {

        Context context = params[0].getContext();
        Log.i("DBUPLOAD", "DBUpload.doInBackground() executing");

        //Timestamp of last time data was uploaded
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long lastTime=prefs.getLong(LAST_UPLOAD,0);

        //Measure this before data upload, so data generated during the uploading isn't skipped.
        Long newTime = System.currentTimeMillis();

        DBHelper dbHelper= new DBHelper(context);
        UploadObject uploadObject = dbHelper.getUploadObjectAfter(lastTime);
        if(uploadObject == null) {
            return null;
        }

        Gson gson = new Gson();
        String json = gson.toJson(uploadObject);
        Log.i("DBUpload", json);

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(context.getResources().getString(R.string.API_URL));

        List<NameValuePair> nvp = new ArrayList<>(1);
        nvp.add(new BasicNameValuePair(API_PARAM,json));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
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
        editor.putLong(LAST_UPLOAD, newTime);
        editor.apply();
        return null;
    }

    protected void onPostExecute(String result) {
    }
}
