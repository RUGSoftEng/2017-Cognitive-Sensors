package main.java.com.teamwan.wander;

import android.app.Application;

import java.util.ArrayList;
import java.lang.Object.android.content.Context;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.GameSession;

/**
 * Class that processes data from the database, ready to be inserted into a graph.
 * CTP stands for correct tap percentage
 * ART stands for average response time
 */

public class GraphData extends Application {
    private ArrayList<Float> CTPAllSessions;
    private ArrayList<Float> ARTAllSessions;

    public GraphData() {
        DBHelper db = new DBHelper(getApplicationContext());

        ArrayList<GameSession> gameSessions = db.getGameSessionsAfter((long)0);

        for(GameSession g:gameSessions) {
            float CTP = 0;
            float ART = 0;
            ArrayList<NumberGuess> ngs = g.getNumberGuesses();
            for(NumberGuess ng:ngs) {
                if(ng.isCorrect()) CTP++;
                ART += ng.getResponseTime();
            }
            this.CTPAllSessions.add(CTP / ngs.size());
            this.ARTAllSessions.add(ART / ngs.size());
        }
    }

    public ArrayList<Float> getCTPAllSessions() {
        return this.CTPAllSessions;
    }

    public ArrayList<Float> getARTAllSessions() {
        return this.ARTAllSessions;
    }

    public float getLatestCTP() {
        return this.CTPAllSessions.get(CTPAllSessions.size());
    }

    public float getLatestART() {
        return this.CTPAllSessions.get(CTPAllSessions.size());
    }
}