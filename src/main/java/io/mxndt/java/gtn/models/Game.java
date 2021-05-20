package io.mxndt.java.gtn.models;

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
}
