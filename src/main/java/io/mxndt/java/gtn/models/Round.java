package io.mxndt.java.gtn.models;

import java.sql.Timestamp;

/**
 * @author mxndt
 */
public class Round {

    private int gameId;
    private int guess;
    private Timestamp timeOfGuess;
    private String result;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public Timestamp getTimeOfGuess() {
        return timeOfGuess;
    }

    public void setTimeOfGuess(Timestamp timeOfGuess) {
        this.timeOfGuess = timeOfGuess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
