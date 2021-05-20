package io.mxndt.java.gtn.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author mxndt
 */
public class Round {

    private int gameId;
    private int guess;
    private Timestamp guessTime;
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

    public Timestamp getGuessTime() {
        return guessTime;
    }

    public void setTimeOfGuess(Timestamp timeOfGuess) {
        this.guessTime = timeOfGuess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 97 * hash + Objects.hashCode(gameId);
        hash = 97 * hash + Objects.hashCode(guessTime);
        hash = 97 * hash + Objects.hashCode(result);
        hash = 97 * hash + Objects.hashCode(guess);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Round other = (Round) obj;
        if (!Objects.equals(gameId, other.gameId)) return false;
        if (!Objects.equals(guessTime, other.guessTime)) return false;
        if (!Objects.equals(result, other.result)) return false;
        if (!Objects.equals(guess, other.guess)) return false;
        return true;
    }
}
