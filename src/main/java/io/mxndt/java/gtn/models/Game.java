package io.mxndt.java.gtn.models;

import java.util.Objects;

/**
 * @author mxndt
 */
public class Game {

    public static final String IN_PROGRESS = "in-progress";
    public static final String FINISHED = "finished";

    private int id;
    private int answer;
    private String status;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 97 * hash + Objects.hashCode(id);
        hash = 97 * hash + Objects.hashCode(answer);
        hash = 97 * hash + Objects.hashCode(status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Game other = (Game) obj;
        if (!Objects.equals(id, other.id)) return false;
        if (!Objects.equals(answer, other.answer)) return false;
        if (!Objects.equals(status, other.status)) return false;
        return true;
    }
}
