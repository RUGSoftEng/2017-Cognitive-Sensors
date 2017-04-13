package com.teamwan.wander.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;


//TODO Replace hardcoded strings by string.xml declaration
public class DBDownload extends AsyncTask<DBpars, Void, Void> {

    private static final String API_URL = "https://script.google.com/macros/s/AKfycbxvbf-dg4ZYc-vFpCCygBgsPpcHl7G31kMmouhhbA6pO-2luQk/exec";
    BufferedReader in = null;
    private String jsonQuestions;

    Context context;

    protected void onPreExecute(){}

    @Override
    protected Void doInBackground(DBpars... params) {

        this.context = params[0].getContext();
//        Log.i("Download_DOINBACKGROUND", "DBUpload.doInBackground() executing");


//        Log.i("GSON_TEST", "Printing JSON of GameSessions");
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Gson gson = new Gson();
//        String jsonQuestions = gson.toJson(uploadObject);
        // Log and System.out.println cut off after 1024chars I think, so debug and a breakpoint at the print to inspect the jsonQuestions string
//          Log.i("GSON_TEST", jsonQuestions);
//        System.out.println("GSONTEST\n"+jsonQuestions);

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(API_URL);

//        try {
//            get.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        try {
            HttpResponse response = client.execute(get);
            Log.d("downloadResponse",response.getStatusLine().toString());
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        String temp = "";
        try {
            while((temp = in.readLine())!= null){
                builder.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonQuestions = builder.toString();

        return null;
    }

    protected void onPostExecute(String result) {
        //Set the questions in the database

        DBHelper dbHelper= new DBHelper(this.context);
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
        ArrayList<Question> questions = gson.fromJson(jsonQuestions, listType);
        dbHelper.insertQuestions(questions);

    }
}
