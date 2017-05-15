package com.teamwan.wander.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Objects;


//TODO Replace hardcoded strings by string.xml declaration
public class DBDownload extends AsyncTask<DBpars, Void, String> {

    private static final String API_URL = "https://script.google.com/macros/s/AKfycbxvbf-dg4ZYc-vFpCCygBgsPpcHl7G31kMmouhhbA6pO-2luQk/exec";
    BufferedReader in = null;


    //Untill response is fixed keep this for testing
//    private final String response = "[{\u0022Start\u0022:true,\u0022NextQuestion\u0022:1,\u0022Question\u0022:\u0022What were you just thinking about?\u0022,\u0022QuestionType\u0022:\u0022MC\u0022,\u0022Answers\u0022:[\u0022I was completely focused on the task.\u0022,\u0022I was evaluating aspects of the task. (For example my performance or how long it is taking)\u0022,\u0022I was thinking about personal matters.\u0022,\u0022I was distracted by my environment. (For example sound, temperature, my physical state)\u0022,\u0022I was daydreaming / I was thinking of task-unrelated matters.\u0022,\u0022I was not paying attention, but neither was I thinking about anything specifically.\u0022]},{\u0022Start\u0022:false,\u0022NextQuestion\u0022:2,\u0022Question\u0022:\u0022How difficult was it to disengage from the thought?\u0022,\u0022QuestionType\u0022:\u0022MC\u0022,\u0022Answers\u0022:[\u0022Very difficult\u0022,\u0022Difficult\u0022,\u0022Neither difficult nor easy\u0022,\u0022Easy\u0022,\u0022Very easy\u0022]},{\u0022Start\u0022:false,\u0022NextQuestion\u0022:\u0022\u0022,\u0022Question\u0022:\u0022What was the valence of your recent thought?\u0022,\u0022QuestionType\u0022:\u0022Slider\u0022}]";
//    private final String response = "[{\\\"Start\\\":true,\\\"NextQuestion\\\":1,\\\"Question\\\":\\\"What were you just thinking about?\\\",\\\"QuestionType\\\":\\\"MC\\\",\\\"Answers\\\":[\\\"I was completely focused on the task.\\\",\\\"I was evaluating aspects of the task. (For example my performance or how long it is taking)\\\",\\\"I was thinking about personal matters.\\\",\\\"I was distracted by my environment. (For example sound, temperature, my physical state)\\\",\\\"I was daydreaming / I was thinking of task-unrelated matters.\\\",\\\"I was not paying attention, but neither was I thinking about anything specifically.\\\"]},{\\\"Start\\\":false,\\\"NextQuestion\\\":2,\\\"Question\\\":\\\"How difficult was it to disengage from the thought?\\\",\\\"QuestionType\\\":\\\"MC\\\",\\\"Answers\\\":[\\\"Very difficult\\\",\\\"Difficult\\\",\\\"Neither difficult nor easy\\\",\\\"Easy\\\",\\\"Very easy\\\"]},{\\\"Start\\\":false,\\\"NextQuestion\\\":\\\"\\\",\\\"Question\\\":\\\"What was the valence of your recent thought?\\\",\\\"QuestionType\\\":\\\"Slider\\\"}]";
//    private final String response = "[{\"Start\":true,\"NextQuestion\":1,\"Question\":\"What were you just thinking about?\",\"QuestionType\":\"MC\",\"Answers\":[\"I was completely focused on the task.\",\"I was evaluating aspects of the task. (For example my performance or how long it is taking)\",\"I was thinking about personal matters.\",\"I was distracted by my environment. (For example sound, temperature, my physical state)\",\"I was daydreaming / I was thinking of task-unrelated matters.\",\"I was not paying attention, but neither was I thinking about anything specifically.\"]},{\"Start\":false,\"NextQuestion\":2,\"Question\":\"How difficult was it to disengage from the thought?\",\"QuestionType\":\"MC\",\"Answers\":[\"Very difficult\",\"Difficult\",\"Neither difficult nor easy\",\"Easy\",\"Very easy\"]},{\"Start\":false,\"NextQuestion\":\"\",\"Question\":\"What was the valence of your recent thought?\",\"QuestionType\":\"Slider\"}]";
    private final String response = "[{\"start\":true,\"nextQuestion\":1,\"question\":\"What were you just thinking about?\",\"questionType\":\"MC\",\"answers\":[\"I was completely focused on the task.\",\"I was evaluating aspects of the task. (For example my performance or how long it is taking)\",\"I was thinking about personal matters.\",\"I was distracted by my environment. (For example sound, temperature, my physical state)\",\"I was daydreaming / I was thinking of task-unrelated matters.\",\"I was not paying attention, but neither was I thinking about anything specifically.\"]},{\"start\":false,\"nextQuestion\":2,\"question\":\"How difficult was it to disengage from the thought?\",\"questionType\":\"MC\",\"answers\":[\"Very difficult\",\"Difficult\",\"Neither difficult nor easy\",\"Easy\",\"Very easy\"]},{\"start\":false,\"nextQuestion\":\"0\",\"question\":\"What was the valence of your recent thought?\",\"questionType\":\"Slider\"}]";

    Context context;

    protected void onPreExecute(){}

    @Override
    protected String doInBackground(DBpars... params) {

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
        //Set the questions in the database

        DBHelper dbHelper= new DBHelper(this.context);
        Gson gson = new GsonBuilder().serializeNulls().create();

        Log.i("DBDownload", "DBDownload onPostExecute()");
        Log.i("DBDownload", result);

//        Question q = new Question();
//        String json = gson.toJson(q);
//        Log.i("DBDownload", json);
//        Question q2 = gson.fromJson(json, new TypeToken<Question>(){}.getQuestionType());
//        assert(Objects.equals(q, q2));


        Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
        ArrayList<Question> questions = gson.fromJson(result, listType);
//      ArrayList<Question> questions = gson.fromJson(response, listType);
        dbHelper.overwriteQuestions(questions);
        ArrayList<Question> q2=dbHelper.getQuestions();
        System.out.println("Assert questions follows:");
        assert(questions == q2);

    }
}
