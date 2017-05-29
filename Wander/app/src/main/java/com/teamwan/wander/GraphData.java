package com.teamwan.wander;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.GameSession;
import com.teamwan.wander.db.QuestionAnswer;

import static java.lang.StrictMath.max;

/**
 * Class that processes data from the database, ready to be inserted into a graph.
 */

public class GraphData {
    private ArrayList<Float> taskCorrectnessAllSessions = new ArrayList<>();
    private ArrayList<Integer> averageResponseAllSessions = new ArrayList<>();
    private ArrayList<GameSession> gameSessions = new ArrayList<>();

    private ArrayList<Float> onTaskCorrectnesses = new ArrayList<>();
    private ArrayList<Float> offTaskCorrectnesses = new ArrayList<>();
    private ArrayList<Integer> onTaskResponses = new ArrayList<>();
    private ArrayList<Integer> offTaskResponses = new ArrayList<>();

    private int totalSessions;

    public GraphData(Context c, int n) {
        DBHelper db = new DBHelper(c);

        gameSessions = db.getGameSessionsAfter((long)0);
        for(GameSession g : gameSessions) {
            this.taskCorrectnessAllSessions.add(g.getPercentage());
            this.averageResponseAllSessions.add(g.getAvg());
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

    public ArrayList<Integer> getOnTaskResponses() {
        return onTaskResponses;
    }

    public ArrayList<Integer> getOffTaskResponses() {
        return offTaskResponses;
    }

    public ArrayList<Float> gettaskCorrectnessAllSessions() { return this.taskCorrectnessAllSessions; }

    public ArrayList<Integer> getAverageResponseAllSessions() { return this.averageResponseAllSessions; }

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

    public List<Integer> getLatestNAverageResponses(int n) {
        int index = max(averageResponseAllSessions.size() - n, 0);
        return this.averageResponseAllSessions.subList(index, taskCorrectnessAllSessions.size());
    }


/**
 * Method seperates data into on task and off-task. A numberguess is considered on task if it precedes an answer to question with
 * questionID == 0 and the answer < 2 and the numberguess is considered off task if the answer > 1.
 */
    public void calculateGraphData(int n){
        for(GameSession g : getLastNGameSessions(n)){
            int i = 0;
            int onTask = 0;
            int offTask = 0;
            int onTaskTime = 0;
            int offTaskTime = 0;
            int onTaskCorrect = 0;
            int offTaskCorrect = 0;
            for(QuestionAnswer qa : g.getQuestionAnswers()) {
                if (qa.getQuestionId() == 0) {
                    while (onTask + offTask < g.getNumberGuesses().size() && g.getNumberGuesses().get(i).getResponseTime() < qa.getTime()) {
                        if (qa.getAnswer() < 2) {
                            //on task
                            onTask++;
                            onTaskTime += g.getNumberGuesses().get(i).getResponseTime();
                            if (g.getNumberGuesses().get(i).isCorrect()) {
                                onTaskCorrect++;
                            }
                        } else {
                            //off task
                            offTask++;
                            offTaskTime += g.getNumberGuesses().get(i).getResponseTime();
                            if (g.getNumberGuesses().get(i).isCorrect()) {
                                offTaskCorrect++;
                            }
                        }
                        i++;
                    }
                }
            }
            onTaskCorrectnesses.add(onTask == 0 ? 0 : (float)onTaskCorrect/(float)onTask);
            offTaskCorrectnesses.add(offTask == 0 ? 0 : (float)offTaskCorrect/(float)offTask);
            onTaskResponses.add(onTask == 0 ? 0 : onTaskTime/onTask);
            offTaskResponses.add(offTask == 0 ? 0 : offTaskTime/offTask);

        }
    }

}