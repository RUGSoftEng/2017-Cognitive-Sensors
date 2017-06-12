package com.teamwan.wander;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.GameSession;
import com.teamwan.wander.db.Question;
import com.teamwan.wander.db.QuestionAnswer;

import static java.lang.StrictMath.max;

/**
 * Class that processes data from the database, ready to be inserted into a graph.
 */

public class GraphData {
    private final ArrayList<Float> taskCorrectnessAllSessions = new ArrayList<>();
    private final ArrayList<Float> averageResponseAllSessions = new ArrayList<>();
    private ArrayList<GameSession> gameSessions = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();

    private final ArrayList<Float> onTaskCorrectnesses = new ArrayList<>();
    private final ArrayList<Float> offTaskCorrectnesses = new ArrayList<>();
    private final ArrayList<Float> onTaskResponses = new ArrayList<>();
    private final ArrayList<Float> offTaskResponses = new ArrayList<>();

    private int totalSessions;

    public GraphData(Context c, int n) {
        DBHelper db = new DBHelper(c);

        gameSessions = db.getGameSessionsAfter((long)0);
        questions = db.getQuestions();
        for(GameSession g : gameSessions) {
            this.taskCorrectnessAllSessions.add(g.getPercentage());
            this.averageResponseAllSessions.add(((float) g.getAvg())/1000);
        }
        calculateGraphData(n);
    }

    public ArrayList<GameSession> getGameSessions() {
        return gameSessions;
    }

    public ArrayList<Float> getOnTaskCorrectnesses() {
        return onTaskCorrectnesses;
    }

    public ArrayList<Float> getOffTaskCorrectnesses() {
        return offTaskCorrectnesses;
    }

    public ArrayList<Float> getOnTaskResponses() {
        return onTaskResponses;
    }

    public ArrayList<Float> getOffTaskResponses() {
        return offTaskResponses;
    }

    public ArrayList<Float> getTaskCorrectnessAllSessions() { return this.taskCorrectnessAllSessions; }

    public ArrayList<Float> getAverageResponseAllSessions() { return this.averageResponseAllSessions; }

    public int getNumberOfSessions(){ return gameSessions.size(); }

    public int getTotalGames(){
        return totalSessions;
    }

    public List<GameSession> getLastNGameSessions(int n){
        int index = max(gameSessions.size() - n, 0);
        return gameSessions.subList(index, gameSessions.size());
    }

    public List<Float> getLatestNTaskCorrectness(int n) {
        int index = max(taskCorrectnessAllSessions.size() - n, 0);
        return this.taskCorrectnessAllSessions.subList(index, taskCorrectnessAllSessions.size());
    }

    public List<Float> getLatestNAverageResponses(int n) {
        int index = max(averageResponseAllSessions.size() - n, 0);
        return this.averageResponseAllSessions.subList(index, taskCorrectnessAllSessions.size());
    }


/**
 * Method seperates data into on task and off-task. A numberguess is considered on task if it precedes the answer given to
 * a question has the onTask value of 1. An onTask value of -1 mean the answer is offtask, and a null value is not defining whether the person was on or offtask.
 */
    public void calculateGraphData(int n){
        for(GameSession g : getLastNGameSessions(n)){
            int ngIndex = 0;
            int onTask = 0;
            int offTask = 0;
            int onTaskTime = 0;
            int offTaskTime = 0;
            int onTaskCorrect = 0;
            int offTaskCorrect = 0;
            for(QuestionAnswer qa : g.getQuestionAnswers()) {
                if (questions.get(qa.getQuestionId()).isDefinesOnTask()) { //The answer to the question defines on/off task
                    while (onTask + offTask < g.getNumberGuesses().size() && g.getNumberGuesses().get(ngIndex).getTime() < qa.getTime()) {
                        if (questions.get(qa.getQuestionId()).getOnOffTask().get(qa.getAnswer()) == 1) {
                            //on task
                            onTask++;
                            onTaskTime += g.getNumberGuesses().get(ngIndex).getResponseTime();
                            if (g.getNumberGuesses().get(ngIndex).isCorrect()) {
                                onTaskCorrect++;
                            }
                        } else if(questions.get(qa.getQuestionId()).getOnOffTask().get(qa.getAnswer()) == -1) {
                            //off task
                            offTask++;
                            offTaskTime += g.getNumberGuesses().get(ngIndex).getResponseTime();
                            if (g.getNumberGuesses().get(ngIndex).isCorrect()) {
                                offTaskCorrect++;
                            }
                        }
                        ngIndex++;
                    }
                }
            }
            onTaskCorrectnesses.add(onTask == 0 ? 0 : (float)onTaskCorrect/(float)onTask);
            offTaskCorrectnesses.add(offTask == 0 ? 0 : (float)offTaskCorrect/(float)offTask);
            onTaskResponses.add(onTask == 0 ? 0 : (float)onTaskTime/(float)onTask);
            offTaskResponses.add(offTask == 0 ? 0 : (float)offTaskTime/(float)offTask);

        }
    }



}