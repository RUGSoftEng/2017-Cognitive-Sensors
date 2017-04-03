package com.teamwan.wander.db;

public class Question {

    private int questionId;
    private boolean start;
    private String type;
    private String question;


    public Question(int questionId, boolean start, String type, String question) {
        this.questionId = questionId;
        this.start = start;
        this.type = type;
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
