package com.teamwan.wander.db;


import java.util.ArrayList;

/* Class that represensts a question object
*
* A question is represented by its id, the question itself and its type.
* Other parameters are needed to store in the database which are the next question to be asked and
* the boolean start that represents the start of a sequence of questions. If a question
* does not have a sequence then the start value for that question will be true.
* */
public class Question {

    private int questionId;
    private boolean start;
    private String type;
    private String question;
    private int nextQuestion;
    private ArrayList<String> mcAnswers;

    public Question(int questionId, boolean start, String type, String question) {
        this.questionId = questionId;
        this.start = start;
        this.type = type;
        this.question = question;
        this.nextQuestion = 0;
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

    public int getNextQuestion() {return nextQuestion; }

    public void setNextQuestion(int nextQuestion) {this.nextQuestion = nextQuestion; }

    public ArrayList<String> getMcAnswers() {
        return mcAnswers;
    }

    public void setMcAnswers(ArrayList<String> mcAnswers) {
        this.mcAnswers = mcAnswers;
    }
}
