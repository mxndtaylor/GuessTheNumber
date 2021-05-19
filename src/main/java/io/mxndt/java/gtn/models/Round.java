package io.mxndt.java.gtn.models;

import java.sql.Time;

/**
 * @author mxndt
 */
public class Round {

    private int gameId;
    private int guess;
    private Time timeOfGuess;
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

    public Time getTimeOfGuess() {
        return timeOfGuess;
    }

    public void setTimeOfGuess(Time timeOfGuess) {
        this.timeOfGuess = timeOfGuess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
