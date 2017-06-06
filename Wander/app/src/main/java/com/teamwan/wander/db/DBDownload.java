package com.teamwan.wander.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamwan.wander.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Download questions from the google sheet named Questions and stores them locally.
 */

public class DBDownload extends AsyncTask<DBpars, Void, String> {

    private BufferedReader in = null;

    private Context context;

    protected void onPreExecute(){}

    @Override
    protected String doInBackground(DBpars... params) {

        this.context = params[0].getContext();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(context.getResources().getString(R.string.API_URL));

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
        String temp;
        try {
            while((temp = in.readLine())!= null){
                builder.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    protected void onPostExecute(String result) {

        DBHelper dbHelper= new DBHelper(this.context);
        Gson gson = new GsonBuilder().serializeNulls().create();

        Log.i("DBDownload", "DBDownload onPostExecute()");
        Log.i("DBDownload", result);


        Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
        ArrayList<Question> questions = gson.fromJson(result, listType);
        dbHelper.overwriteQuestions(questions);
        ArrayList<Question> q2=dbHelper.getQuestions();
        System.out.println("Assert questions follows:");
        assert(questions == q2);

    }
}
