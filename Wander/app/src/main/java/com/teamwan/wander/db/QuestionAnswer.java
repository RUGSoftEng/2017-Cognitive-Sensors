package com.teamwan.wander.db;

/* Class that represensts a question's answer object
*
*In this class we can find the answers given by the player to the questions that were
*asked during the game session and at what time the answer was given.
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
