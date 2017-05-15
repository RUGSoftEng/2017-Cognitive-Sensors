package com.teamwan.wander.db;


import java.util.ArrayList;

/* Class that represents a question object
*
* A question is represented by its questionID, the question itself and its questionType.
* Other parameters are needed to store in the database which are the next question to be asked and
* the boolean start that represents the start of a sequence of questions.
* */
public class Question {

    private int questionId;
    private boolean start;
    private String questionType;
    private String question;
    private String nextQuestion;
    private ArrayList<String> answers;

    public Question(int questionId, boolean start, String type, String question) {
        this.questionId = questionId;
        this.start = start;
        this.questionType = type;
        this.question = question;
        this.nextQuestion = Integer.toString(0);
    }

    public Question(){}

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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNextQuestion() {
        if(nextQuestion == null || nextQuestion.equals("")){
            return 0;
        }
        return Integer.valueOf(nextQuestion);
    }

    public void setNextQuestion(int nextQuestion) {this.nextQuestion = Integer.toString(nextQuestion); }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
