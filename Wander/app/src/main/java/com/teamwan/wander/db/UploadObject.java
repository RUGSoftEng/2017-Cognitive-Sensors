package com.teamwan.wander.db;

import java.util.ArrayList;

/**
 * Class that represents the final object that is going to be sent to the server database.
 * It is composed by the player id and by the corresponding game sessions, game types, the numberGuesses and the corresponding question's answers.
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

    public void setGameSessions(ArrayList<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }
}
