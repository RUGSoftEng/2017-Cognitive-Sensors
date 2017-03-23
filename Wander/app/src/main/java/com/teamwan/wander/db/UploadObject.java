package com.teamwan.wander.db;

import com.teamwan.wander.GameSession;

import java.util.ArrayList;

/**
 * Created by lore on 23/03/2017.
 */

public class UploadObject {

    private Integer playerId;
    private ArrayList<GameSession> gameSessions;


    public UploadObject(Integer playerId, ArrayList<GameSession> gameSessionss) {
        this.playerId = playerId;
        this.gameSessions = gameSessionss;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public ArrayList<GameSession> getGameSessions() {
        return gameSessions;
    }

    public void setGameSessionss(ArrayList<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }
}
