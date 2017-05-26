package com.teamwan.wander;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.GameSession;
import com.teamwan.wander.db.QuestionAnswer;

import static java.lang.StrictMath.max;

/**
 * Class that processes data from the database, ready to be inserted into a graph.
 * CTP stands for correct tap percentage
 * ART stands for average response time
 */

public class GraphData {
    private ArrayList<Float> CTPAllSessions;
    private ArrayList<Integer> ARTAllSessions;
    private ArrayList<GameSession> gameSessions;

    private ArrayList<Float> onTaskCorrectnesses = new ArrayList<>();
    private ArrayList<Float> offTaskCorrectnesses = new ArrayList<>();
    private ArrayList<Integer> onTaskResponses = new ArrayList<>();
    private ArrayList<Integer> offTaskResponses = new ArrayList<>();

    private int totalSessions;

    public GraphData(Context c) {
        DBHelper db = new DBHelper(c);

        gameSessions = db.getGameSessionsAfter((long)0);

        for(GameSession g : gameSessions) {
            this.CTPAllSessions.add(g.getPercentage());
            this.ARTAllSessions.add(g.getAvg());
        }
    }

    public ArrayList<GameSession> getGameSessions() {
        return gameSessions;
    }

    public ArrayList<GameSession> getLastNGameSessions(int n){
        int index = max(gameSessions.size() - n, 0);
        return (ArrayList<GameSession>)gameSessions.subList(index, gameSessions.size());
    }

    public void setGameSessions(ArrayList<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }

    public ArrayList<Float> getCTPAllSessions() {
        return this.CTPAllSessions;
    }

    public ArrayList<Integer> getARTAllSessions() {
        return this.ARTAllSessions;
    }

    public ArrayList<Float> getLatestNCTP(int n) {
        int index = max(CTPAllSessions.size() - n, 0);
        return (ArrayList<Float>) this.CTPAllSessions.subList(index, CTPAllSessions.size());
    }

    public ArrayList<Integer> getLatestART(int n) {
        int index = max(ARTAllSessions.size() - n, 0);
        return (ArrayList<Integer>) this.ARTAllSessions.subList(index, CTPAllSessions.size());
    }

    public int getTotalGames(){
        return totalSessions;
    }

    public void calculateGraphData(int n){
        for(GameSession g : getLastNGameSessions(n)){
            int i = 0;
            int onTask = 0;
            int offTask = 0;
            int onTaskTime = 0;
            int offTaskTime = 0;
            int onTaskCorrect = 0;
            int offTaskCorrect = 0;
            for(QuestionAnswer qa : g.getQuestionAnswers()){
                if(qa.getQuestionId() == 0){
                    while(g.getNumberGuesses().get(i).getResponseTime() < qa.getTime()){
                        if(qa.getAnswer() < 2){
                            //on task
                            onTask++;
                            onTaskTime += g.getNumberGuesses().get(i).getResponseTime();
                            if(g.getNumberGuesses().get(i).isCorrect()){
                                onTaskCorrect++;
                            }
                        } else {
                            //off task
                            offTask++;
                            offTaskTime += g.getNumberGuesses().get(i).getResponseTime();
                            if(g.getNumberGuesses().get(i).isCorrect()){
                                offTaskCorrect++;
                            }
                        }
                        i++;
                    }

                }
            }
            onTaskCorrectnesses.add((float)onTaskCorrect/(float)onTask);
            offTaskCorrectnesses.add((float)offTaskCorrect/(float)offTask);
            onTaskResponses.add(onTaskTime/onTask);
            offTaskResponses.add(offTaskTime/offTask);

        }
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
}