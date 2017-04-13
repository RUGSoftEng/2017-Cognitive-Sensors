package com.teamwan.wander.db;

/**
 * Created by lore on 23/03/2017.
 */

public class QuestionAnswer {

    private int questionId;
    private long time;
    private int answer;

    public QuestionAnswer(long time, int questionId, int answer) {
        this.time = time;
        this.questionId = questionId;
        this.answer = answer;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
